package dev.icerock.moko.permissions.definitions

import web.permissions.PermissionDescriptor
import web.permissions.PermissionStatus
import kotlin.js.Promise

/**
 * https://developer.mozilla.org/en-US/docs/Web/API/Permissions
 */
@JsNonModule
external class Permissions {
    /**
     * https://developer.mozilla.org/en-US/docs/Web/API/Permissions/query
     */
    fun query(descriptor: PermissionDescriptor): Promise<PermissionStatus>
}
