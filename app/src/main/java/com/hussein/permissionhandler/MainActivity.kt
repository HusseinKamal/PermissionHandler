package com.hussein.permissionhandler

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.shouldShowRationale
import com.hussein.permissionhandler.ui.theme.PermissionHandlerTheme

@ExperimentalPermissionsApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PermissionHandlerTheme {
                val permission = rememberMultiplePermissionsState(permissions = listOf(
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CAMERA,
                ))
                val lifecyclerOwner = LocalLifecycleOwner.current
                DisposableEffect(key1 = lifecyclerOwner, effect = {
                    val observer = LifecycleEventObserver{ _,event->
                        //Here you can use onStart,onResume,onCreate,onDestroy all lifecycles methods
                        if(event == Lifecycle.Event.ON_RESUME){
                            permission.launchMultiplePermissionRequest()
                        }
                    }
                    lifecyclerOwner.lifecycle.addObserver(observer = observer)
                     onDispose {
                         lifecyclerOwner.lifecycle.removeObserver(observer)
                     }
                })
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    permission.permissions.forEach { perm ->
                        when(perm.permission){
                            Manifest.permission.RECORD_AUDIO -> {
                                when {
                                    perm.status.isGranted -> {
                                        Text(text = "Camera Permission accepted")
                                    }
                                    perm.status.shouldShowRationale -> {
                                        Text(text = "Camera Permission is needed to access Camera")
                                    }
                                    perm.isPermanentDenied() -> {
                                        Text(text = "Camera Permission was permanently denied access .You can enable it in the app settings")
                                    }
                                }
                            }
                            Manifest.permission.CAMERA ->{
                                when {
                                    perm.status.isGranted -> {
                                        Text(text = "Audio Permission accepted")
                                    }
                                    perm.status.shouldShowRationale -> {
                                        Text(text = "Audio Permission is needed to access Record Audio")
                                    }
                                    perm.isPermanentDenied() -> {
                                        Text(text = "Audio Permission was permanently denied access .You can enable it in the app settings")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    PermissionHandlerTheme {
        Greeting("Android")
    }
}