package kyge.triggerEvents

import kyge.builder
import kyge.indent
import kyge.plusAssign


interface WebhookEventType<WebhookEventInterface>

interface WebhookEventInterface<T> {
    val name: String
    operator fun invoke() {
        indent {
            builder += "$name:"
        }
    }

    operator fun <E> invoke(vararg types: E) where E : Enum<E>, E : WebhookEventType<T> {
        indent {
            builder += "$name:"
            indent {
                builder += "types: [${types.joinToString { it.name }}]"
            }
        }
    }
}

interface WebhookEvent {
    object checkRun : WebhookEventInterface<checkRun> {
        override val name: String get() = "check_run"

        enum class Type : WebhookEventType<checkRun> { create, rerequested, completed }
    }

    object checkSuite : WebhookEventInterface<checkSuite> {
        override val name: String get() = "check_suite"

        enum class Type : WebhookEventType<checkSuite> { completed, requested, rerequested }
    }

    object create : WebhookEventInterface<create> {
        override val name: String get() = "create"
    }

    object delete : WebhookEventInterface<delete> {
        override val name: String get() = "delete"
    }

    object deployment : WebhookEventInterface<deployment> {
        override val name: String get() = "deployment"
    }

    object deploymentStatus : WebhookEventInterface<deploymentStatus> {
        override val name: String get() = "deployment_status"
    }

    object discussion : WebhookEventInterface<discussion> {
        override val name: String get() = "discussion"

        enum class Type : WebhookEventType<discussion> { created, edited, deleted, transferred, pinned, unpinned, labeled, unlabeled, locked, unlocked, category_changed, answered, unanswered }
    }

    object discussionComment : WebhookEventInterface<discussionComment> {
        override val name: String get() = "discussion_comment"

        enum class Type : WebhookEventType<discussionComment> { created, edited, deleted }
    }

    object fork : WebhookEventInterface<fork> {
        override val name: String get() = "fork"
    }

    object gollum : WebhookEventInterface<gollum> {
        override val name: String get() = "gollum"
    }

    object issueComment : WebhookEventInterface<issueComment> {
        override val name: String get() = "issue_comment"

        enum class Type : WebhookEventType<issueComment> { created, edited, deleted }
    }

    object issues : WebhookEventInterface<issues> {
        override val name: String get() = "issues"

        enum class Type : WebhookEventType<issues> { opened, edited, deleted, transferred, pinned, unpinned, closed, reopened, assigned, unassigned, labeled, unlabeled, locked, unlocked, milestoned, demilestoned }
    }

    object label : WebhookEventInterface<label> {
        override val name: String get() = "label"

        enum class Type : WebhookEventType<label> { created, edited, deleted }
    }

    object milestone : WebhookEventInterface<milestone> {
        override val name: String get() = "milestone"

        enum class Type : WebhookEventType<milestone> { created, closed, opened, edited, deleted }
    }

    object pageBuild : WebhookEventInterface<pageBuild> {
        override val name: String get() = "page_build"
    }

    object project : WebhookEventInterface<project> {
        override val name: String get() = "project"

        enum class Type : WebhookEventType<project> { created, updated, closed, reopened, edited, deleted }
    }

    object projectCard : WebhookEventInterface<projectCard> {
        override val name: String get() = "project_card"

        enum class Type : WebhookEventType<projectCard> { created, moved, converted, edited, deleted }
    }

    object projectColumn : WebhookEventInterface<projectColumn> {
        override val name: String get() = "project_column"

        enum class Type : WebhookEventType<projectColumn> { created, updated, moved, deleted }
    }

    object public : WebhookEventInterface<public> {
        override val name: String get() = "public"
    }

    object pullRequest : WebhookEventInterface<pullRequest>, PushPullRequestFeatures {
        override val name: String get() = "pull_request"

        enum class Type : WebhookEventType<pullRequest> { assigned, unassigned, labeled, unlabeled, opened, edited, closed, reopened, synchronize, converted_to_draft, ready_for_review, locked, unlocked, review_requested, review_request_removed, auto_merge_enabled, auto_merge_disabled }
    }

    object pullRequestReview : WebhookEventInterface<pullRequestReview> {
        override val name: String get() = "pull_request_review"

        enum class Type : WebhookEventType<pullRequestReview> { submitted, edited, dismissed }
    }

    object pullRequestReviewComment : WebhookEventInterface<pullRequestReviewComment> {
        override val name: String get() = "pull_request_review_comment"

        enum class Type : WebhookEventType<pullRequestReviewComment> { created, edited, deleted }
    }

    object pullRequestTarget : WebhookEventInterface<pullRequestTarget> {
        override val name: String get() = "pull_request_target"

        enum class Type : WebhookEventType<pullRequestTarget> { assigned, unassigned, labeled, unlabeled, opened, edited, closed, reopened, synchronize, converted_to_draft, ready_for_review, locked, unlocked, review_requested, review_request_removed, auto_merge_enabled, auto_merge_disabled }
    }

    object push : WebhookEventInterface<push>, PushPullRequestFeatures {
        override val name: String get() = "push"
    }

    object registryPackage : WebhookEventInterface<registryPackage> {
        override val name: String get() = "registry_package"

        enum class Type : WebhookEventType<registryPackage> { published, updated }
    }

    object release : WebhookEventInterface<release> {
        override val name: String get() = "release"

        enum class Type : WebhookEventType<release> { published, unpublished, created, edited, deleted, prereleased, released }
    }

    object status : WebhookEventInterface<status> {
        override val name: String get() = "status"
    }

    object watch : WebhookEventInterface<watch> {
        override val name: String get() = "watch"

        enum class Type : WebhookEventType<watch> { started }
    }

    object workflowRun : WebhookEventInterface<workflowRun> {
        override val name: String get() = "workflow_run"

        enum class Type : WebhookEventType<workflowRun> { completed, requested }
    }
}

interface PushPullRequestFeatures {

    operator fun invoke(block: PushPullRequestFeatures.() -> Unit) = block()

    fun func(name: String, block: Builder.() -> Unit) {
        indent {
            builder += "$name:"
            Builder.block()
        }
    }

    fun func(name: String, vararg branches: String) {
        indent {
            builder += "$name:"
            indent {
                for (branch in branches)
                    builder += "- '$branch'"
            }
        }
    }

    fun branches(block: Builder.() -> Unit) = func("branches", block)
    fun ignoreBranches(block: Builder.() -> Unit) = func("branches-ignore", block)

    fun branches(vararg branches: String) = func("branches", *branches)
    fun ignoreBranches(vararg branches: String) = func("branches-ignore", *branches)

    fun tags(block: Builder.() -> Unit) = func("tags", block)
    fun ignoreTags(block: Builder.() -> Unit) = func("tags-ignore", block)

    fun tags(vararg tags: String) = func("tags", *tags)
    fun ignoreTags(vararg tags: String) = func("tags-ignore", *tags)

    fun paths(block: Builder.() -> Unit) = func("paths", block)
    fun ignorePaths(block: Builder.() -> Unit) = func("paths-ignore", block)

    fun paths(vararg paths: String) = func("paths", *paths)
    fun ignorePaths(vararg paths: String) = func("paths-ignore", *paths)

    object Builder {
        operator fun String.unaryPlus() = indent { builder += "- '$this'" }
        operator fun String.unaryMinus() = indent { builder += "- '!$this'" }
    }
}