package dev.icerock.moko.permissions

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.notifications.Notification
import web.permissions.Permissions

class JsPermissionController : PermissionsController {
    // https://developer.mozilla.org/en-US/docs/Web/API/Permissions_API

    override suspend fun providePermission(permission: Permission) {
        val navigator: dynamic = window.navigator
        val permissions = navigator.permissions() as Permissions

        when (permission) {
            Permission.CAMERA -> TODO() // permissions.query()
            Permission.GALLERY -> TODO()
            Permission.STORAGE -> TODO()
            Permission.WRITE_STORAGE -> TODO()
            Permission.LOCATION -> TODO()
            Permission.COARSE_LOCATION -> TODO()
            Permission.BLUETOOTH_LE -> TODO()
            Permission.REMOTE_NOTIFICATION -> Notification.requestPermission().await()
            Permission.RECORD_AUDIO -> TODO()
            Permission.BLUETOOTH_SCAN -> TODO()
            Permission.BLUETOOTH_ADVERTISE -> TODO()
            Permission.BLUETOOTH_CONNECT -> TODO()
        }
    }

    override suspend fun isPermissionGranted(permission: Permission): Boolean {
        when (permission) {
            Permission.CAMERA -> TODO()
            Permission.GALLERY -> TODO()
            Permission.STORAGE -> TODO()
            Permission.WRITE_STORAGE -> TODO()
            Permission.LOCATION -> TODO()
            Permission.COARSE_LOCATION -> TODO()
            Permission.BLUETOOTH_LE -> TODO()
            Permission.REMOTE_NOTIFICATION -> TODO()
            Permission.RECORD_AUDIO -> TODO()
            Permission.BLUETOOTH_SCAN -> TODO()
            Permission.BLUETOOTH_ADVERTISE -> TODO()
            Permission.BLUETOOTH_CONNECT -> TODO()
        }
    }

    override suspend fun getPermissionState(permission: Permission): PermissionState {
        TODO("Not yet implemented")
    }

    override fun openAppSettings() {
        TODO("Not yet implemented")
    }
}
