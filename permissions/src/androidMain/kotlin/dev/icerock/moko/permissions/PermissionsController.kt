/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.permissions

import android.Manifest
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import kotlin.coroutines.suspendCoroutine

actual class PermissionsController(
    val resolverFragmentTag: String = "PermissionsControllerResolver"
) {
    var fragmentManager: FragmentManager? = null

    fun bind(lifecycle: Lifecycle, fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager

        val observer = object : LifecycleObserver {

            @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
            fun onDestroyed(source: LifecycleOwner) {
                this@PermissionsController.fragmentManager = null
                source.lifecycle.removeObserver(this)
            }
        }
        lifecycle.addObserver(observer)
    }

    actual suspend fun providePermission(permission: Permission) {
        val fragmentManager =
            fragmentManager
                ?: throw IllegalStateException("can't resolve permission without active window")

        val currentFragment: Fragment? = fragmentManager.findFragmentByTag(resolverFragmentTag)
        val resolverFragment: ResolverFragment = if (currentFragment != null) {
            currentFragment as ResolverFragment
        } else {
            ResolverFragment().apply {
                fragmentManager
                    .beginTransaction()
                    .add(this, resolverFragmentTag)
                    .commitNow()
            }
        }

        val platformPermission = permission.toPlatformPermission()
        suspendCoroutine<Unit> { continuation ->
            resolverFragment.requestPermission(permission, platformPermission) { continuation.resumeWith(it) }
        }
    }

    private fun Permission.toPlatformPermission(): String {
        return when (this) {
            Permission.CAMERA -> Manifest.permission.CAMERA
            Permission.GALLERY -> Manifest.permission.READ_EXTERNAL_STORAGE
            Permission.STORAGE -> Manifest.permission.READ_EXTERNAL_STORAGE
            Permission.LOCATION -> Manifest.permission.ACCESS_FINE_LOCATION
            Permission.COARSE_LOCATION -> Manifest.permission.ACCESS_COARSE_LOCATION
        }
    }

    class ResolverFragment : Fragment() {
        init {
            retainInstance = true
        }

        private val permissionCallbackMap = mutableMapOf<Int, PermissionCallback>()

        fun requestPermission(permission: Permission, permissionCode: String, callback: (Result<Unit>) -> Unit) {
            val context = requireContext()
            if (ContextCompat.checkSelfPermission(context, permissionCode) ==
                PackageManager.PERMISSION_GRANTED
            ) {
                callback.invoke(Result.success(Unit))
                return
            }

            val requestCode = permissionCallbackMap.keys.sorted().lastOrNull() ?: 0
            permissionCallbackMap[requestCode] = PermissionCallback(permission, callback)

            requestPermissions(arrayOf(permissionCode), requestCode)
        }

        override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        ) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)

            val permissionCallback = permissionCallbackMap[requestCode] ?: return
            permissionCallbackMap.remove(requestCode)

            val success = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (success) {
                permissionCallback.callback.invoke(Result.success(Unit))
            } else {
                if (shouldShowRequestPermissionRationale(permissions.first())) {
                    permissionCallback.callback.invoke(Result.failure(DeniedException(permissionCallback.permission)))
                } else {
                    permissionCallback.callback.invoke(Result.failure(DeniedAlwaysException(permissionCallback.permission)))
                }
            }
        }

        private class PermissionCallback(
            val permission: Permission,
            val callback: (Result<Unit>) -> Unit
        )
    }
}
