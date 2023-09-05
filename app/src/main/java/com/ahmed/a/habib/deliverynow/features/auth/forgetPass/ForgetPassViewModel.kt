package com.ahmed.a.habib.deliverynow.features.auth.forgetPass

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.a.habib.deliverynow.R
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ForgetPassViewModel @Inject constructor() : ViewModel() {

    val channel = Channel<ForgetPassIntents>(Channel.UNLIMITED)

    private val _result = MutableSharedFlow<ForgetPassViewStates>()
    val result: SharedFlow<ForgetPassViewStates> get() = _result

    init {
        sendIntent()
    }

    private fun sendIntent() = viewModelScope.launch {
        channel.consumeAsFlow().collect {
            when (it) {
                is ForgetPassIntents.SendMail -> sendEmail(it.email)
            }
        }
    }

    private fun sendEmail(email: String) = viewModelScope.launch {
        _result.emit(ForgetPassViewStates.Loading)

        if (email.isEmpty()) {
            _result.emit(ForgetPassViewStates.ShowResError(R.string.email_is_required))
        } else {
            try {
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener {
                    viewModelScope.launch {
                        if (it.isSuccessful) {
                            _result.emit(ForgetPassViewStates.MailIsSent(email))
                        }
                    }
                }.await()
            } catch (e: Exception) {
                _result.emit(ForgetPassViewStates.ShowMsgError(e.message.orEmpty()))
            }
        }
    }
}