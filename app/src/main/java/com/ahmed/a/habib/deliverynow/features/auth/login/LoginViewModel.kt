package com.ahmed.a.habib.deliverynow.features.auth.login

import android.app.Activity
import android.content.Intent
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.a.habib.deliverynow.BaseApp
import com.ahmed.a.habib.deliverynow.R
import com.ahmed.a.habib.deliverynow.features.auth.User
import com.ahmed.a.habib.deliverynow.dataStore.DeliveryNowDataStoreImp
import com.ahmed.a.habib.deliverynow.utils.SharedKeys
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStore: DeliveryNowDataStoreImp
) : ViewModel() {

    val channel = Channel<LoginIntents>(Channel.UNLIMITED)

    private val _results = MutableSharedFlow<LoginViewStates>()
    val results: SharedFlow<LoginViewStates> get() = _results

    init {
        sendIntent()
    }

    private fun sendIntent() = viewModelScope.launch {
        channel.consumeAsFlow().collect {
            when (it) {
                is LoginIntents.ValidateAndLogin -> validateAndLogin(it.email, it.password)
                is LoginIntents.GetGoogleAccount -> getGoogleAccount(it.activity, it.callback)
                is LoginIntents.SignInWithGoogle -> signInWithGoogle(it.activityResult)
                is LoginIntents.GetUserFromFireStoreAndCashIt -> getUserFromFireStore(it.user)
            }
        }
    }

    private fun validateAndLogin(mail: String, password: String) = viewModelScope.launch {
        if (mail.isNotEmpty() && password.isNotEmpty()) {
            login(mail, password)
        } else if (mail.isEmpty() && password.isEmpty()) {
            _results.emit(LoginViewStates.ResError(R.string.email_and_password_are_required))
        } else if (password.isEmpty()) {
            _results.emit(LoginViewStates.ResError(R.string.password_is_required))
        } else if (mail.isEmpty()) {
            _results.emit(LoginViewStates.ResError(R.string.email_is_required))
        }
    }

    private fun login(mail: String, password: String) = viewModelScope.launch {
        _results.emit(LoginViewStates.Loading)

        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(mail, password)
                .addOnCompleteListener {
                    viewModelScope.launch {
                        if (it.isSuccessful) {
                            _results.emit(
                                LoginViewStates.IsLoggedIn(
                                    User(
                                        id = it.result.user?.uid.orEmpty(),
                                        name = it.result.user?.displayName.orEmpty(),
                                        email = mail,
                                        password = password,
                                        userType = BaseApp.getUserType()
                                    )
                                )
                            )
                        }
                    }
                }.await()
        } catch (e: Exception) {
            _results.emit(LoginViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun getGoogleAccount(
        activity: Activity,
        callback: ManagedActivityResultLauncher<Intent, ActivityResult>
    ) = viewModelScope.launch {

        _results.emit(LoginViewStates.Loading)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val intent = googleSignInClient.signInIntent

        callback.launch(intent)
    }

    private fun signInWithGoogle(activityResult: ActivityResult) = viewModelScope.launch {
        try {
            val task = GoogleSignIn.getSignedInAccountFromIntent(activityResult.data)
            val account = task.getResult(ApiException::class.java)
            val credential = GoogleAuthProvider.getCredential(account.idToken, null)

            FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                viewModelScope.launch {
                    if (it.isSuccessful) {
                        _results.emit(
                            LoginViewStates.IsLoggedIn(
                                User(
                                    id = it.result.user?.uid.orEmpty(),
                                    userType = BaseApp.getUserType(),
                                    email = it.result.user?.email.orEmpty(),
                                    name = it.result.user?.displayName.orEmpty().ifEmpty { "--" },
                                    password = "GOOGLE_SIGNING"
                                )
                            )
                        )
                    }
                }
            }.await()
        } catch (e: Exception) {
            _results.emit(LoginViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun getUserFromFireStore(user: User) = viewModelScope.launch {
        try {
            Firebase.firestore.collection(SharedKeys.usersCollection).document(user.id).get()
                .addOnCompleteListener {
                    if (it.result.getString("id").orEmpty().isEmpty()
                        && it.result.getString("name").orEmpty().isEmpty()
                        && it.result.getString("email").orEmpty().isEmpty()
                    ) {
                        saveUser(user)
                    } else {
                        checkForPasswordIsChangedAndCashUser(
                            User(
                                id = it.result.getString("id").orEmpty(),
                                name = it.result.getString("name").orEmpty(),
                                email = it.result.getString("email").orEmpty(),
                                imgUrl = it.result.getString("imgUrl").orEmpty(),
                                password = user.password,
                                userType = it.result.getString("userType").orEmpty(),
                            )
                        )
                    }
                }.await()
        } catch (e: Exception) {
            _results.emit(LoginViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun checkForPasswordIsChangedAndCashUser(user: User) = viewModelScope.launch {
        try {
            if (dataStore.getCashedUser().password != user.password) {

                Firebase.firestore.runBatch { batch ->
                    val personRef = Firebase
                        .firestore
                        .collection(SharedKeys.usersCollection)
                        .document(user.id)

                    batch.update(personRef, "password", user.password)
                }.await()
            }

            cashUser(user)

        } catch (e: Exception) {
            _results.emit(LoginViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun saveUser(user: User) = viewModelScope.launch {
        try {
            cashUser(user)
            Firebase.firestore
                .collection(SharedKeys.usersCollection)
                .document(user.id)
                .set(user)
                .await()
        } catch (e: Exception) {
            _results.emit(LoginViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun cashUser(user: User) = viewModelScope.launch {
        try {
            dataStore.cashUser(user)
            _results.emit(LoginViewStates.UserIsCashed)
        } catch (e: Exception) {
            _results.emit(LoginViewStates.MsgError(e.message.orEmpty()))
        }
    }
}