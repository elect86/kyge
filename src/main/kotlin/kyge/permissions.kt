package kyge


interface PermissionsInterface {

    var permissions: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            val permissions = when (value) {
                Permission.readAndWrite -> "read-all|write-all"
                Permission.read -> "read-all"
                Permission.write -> "write-all"
                else -> "none"
            }
            builder += "permissions: $permissions"
        }

    fun permissions(block: Permissions.() -> Unit) {
        builder += "permissions:"
        indent {
            Permissions.block()
        }
    }
}

enum class Permission {
    none, read, write, readAndWrite;

    override fun toString() = when (this) {
        readAndWrite -> "read|write"
        else -> name
    }
}

object Permissions {
    var actions: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "actions: $value"
        }
    var checks: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "checks: $value"
        }
    var contents: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "contents: $value"
        }
    var deployments: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "deployments: $value"
        }
    var issues: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "issues: $value"
        }
    var discussions: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "discussions: $value"
        }
    var packages: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "packages: $value"
        }
    var pullRequests: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "pull-requests: $value"
        }
    var repositoryProjects: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "repository-projects: $value"
        }
    var securityEvents: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "security-events: $value"
        }
    var statuses: Permission
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "statuses: $value"
        }
}