package com.ahmed.a.habib.deliverynow.features.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.a.habib.deliverynow.R
import com.ahmed.a.habib.deliverynow.features.auth.User
import com.ahmed.a.habib.deliverynow.dataStore.DeliveryNowDataStoreImp
import com.ahmed.a.habib.deliverynow.utils.SharedKeys.Companion.usersCollection
import com.google.firebase.auth.FirebaseAuth
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
class RegisterViewModel @Inject constructor(
    private val dataStore: DeliveryNowDataStoreImp
) : ViewModel() {

    val channel = Channel<RegisterIntents>(Channel.UNLIMITED)

    private val _results = MutableSharedFlow<RegisterViewStates>()
    val results: SharedFlow<RegisterViewStates> get() = _results

    init {
        sendIntent()
    }

    private fun sendIntent() {
        viewModelScope.launch {
            channel.consumeAsFlow().collect {
                when (it) {
                    is RegisterIntents.ValidateAndRegister -> validateAndRegister(it.user)
                    is RegisterIntents.CashUser -> cashUser(it.user)
                }
            }
        }
    }

    private fun validateAndRegister(user: User) = viewModelScope.launch {
        if (user.email.isNotEmpty() && user.password.isNotEmpty() && user.name.isNotEmpty()) {
            register(user)
        } else if (user.email.isEmpty() && user.password.isEmpty() && user.name.isEmpty()) {
            _results.emit(RegisterViewStates.ResError(R.string.email_and_password_and_name_are_required))
        } else if (user.password.isEmpty()) {
            _results.emit(RegisterViewStates.ResError(R.string.password_is_required))
        } else if (user.name.isEmpty()) {
            _results.emit(RegisterViewStates.ResError(R.string.name_is_required))
        } else if (user.email.isEmpty()) {
            _results.emit(RegisterViewStates.ResError(R.string.email_is_required))
        }
    }

    private fun register(user: User) = viewModelScope.launch {
        _results.emit(RegisterViewStates.Loading)

        try {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    viewModelScope.launch {
                        if (it.isSuccessful) {
                            user.id = it.result.user?.uid.orEmpty()
                            saveUser(user)
                        }
                    }
                }.await()
        } catch (e: Exception) {
            _results.emit(RegisterViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun saveUser(user: User) = viewModelScope.launch {
        try {
            _results.emit(RegisterViewStates.IsRegistered(user))
            Firebase.firestore.collection(usersCollection).document(user.id).set(user).await()
        } catch (e: Exception) {
            deleteCurrentUser(user)
            _results.emit(RegisterViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun cashUser(user: User) = viewModelScope.launch {
        try {
            dataStore.cashUser(user)
            _results.emit(RegisterViewStates.UserIsCashed)
        } catch (e: Exception) {
            _results.emit(RegisterViewStates.MsgError(e.message.orEmpty()))
        }
    }

    private fun deleteCurrentUser(user: User) = viewModelScope.launch {
        try {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(user.email, user.password)
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        FirebaseAuth.getInstance().currentUser?.delete()
                    }
                }.await()
        } catch (e: Exception) {
            _results.emit(RegisterViewStates.MsgError(e.message.orEmpty()))
        }
    }
}