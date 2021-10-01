package kyge


interface Permissions {

    fun permissions(readAll: Boolean = false,
                    writeAll: Boolean = false) {

        val permissions = when {
            readAll && writeAll -> "read-all|write-all"
            readAll -> "read-all"
            else -> "write-all"
        }
        builder += "permissions: $permissions"
    }

    fun permissions(block: Builder.() -> Unit) {
        builder += "permissions:"
        indent {
            Builder.block()
        }
    }

    object Builder {
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

    enum class Permission { none, read, write, readAndWrite;

        override fun toString() = when(this) {
            readAndWrite -> "read|write"
            else -> name
        }
    }
}