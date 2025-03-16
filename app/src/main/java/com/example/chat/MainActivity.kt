package com.example.chat

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import com.elvishew.xlog.XLog.init
import com.example.chat.ui.theme.ChatTheme
import com.zegocloud.zimkit.components.conversation.ui.ZIMKitConversationFragment
import com.zegocloud.zimkit.services.ZIMKit
import com.zegocloud.zimkit.services.ZIMKit.connectUser
import com.zegocloud.zimkit.services.ZIMKitConfig
import com.zegocloud.zimkit.services.model.ZIMKitConversation
import im.zego.zim.enums.ZIMErrorCode

class MainActivity : FragmentActivity() {

    private var openConversations by mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        init()
        setContent {
            ChatTheme {


            }
        }

        @Composable
        fun Screen(modifier: Modifier = Modifier) {
            if (openConversations) {

            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = {
                            connectUser()
                        }
                    ) {
                        Text(text = "Open Conversations")
                    }
                }

            }
        }
    }


    @Composable
    fun ConversationsScreen(modifier: Modifier = Modifier) {
        val fragmentManager = remember {
            this@MainActivity.supportFragmentManager
        }
        val fragment = remember {
            ZIMKitConversationFragment()
        }
        AndroidView (
            modifier = modifier,
            factory = {
                FrameLayout(it).apply {
                    id = View.generateViewId()
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )

                }
            },
            update = {
                fragmentManager.beginTransaction()
                    .replace(it.id, fragment)
                    .commit()
            }
        )
    }


        private fun connectUser() {
            val userId = "user1"
            val useName = "user1"
            val userImage = "https://storage.zego.im/IMKit/avatar-0.png"

            ZIMKit.connectUser(userId, useName, userImage) {info ->
                if (info.code == ZIMErrorCode.SUCCESS) {
                    openConversations = true
                } else {
                    Toast.makeText( this,
                        "Connect user failed: ${info.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

        }

        fun init() {
            val appID = 4L
            val appSign = "433"
            ZIMKit.initWith(application, appID, appSign, ZIMKitConfig())
            ZIMKit.initNotifications()
        }
    }


