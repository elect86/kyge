package kyge.triggerEvents

import kyge.*
import kyge.builder
import kyge.triggerEvents.PushPullRequestFeatures.Builder.unaryPlus


interface WebhookEventType<WebhookEventInterface>

interface WebhookEventInterface<T> {
    val name: String

    fun <E : Enum<E>> types(vararg types: E) {
        //        indent {
        //            builder += "$name:"
        indent {
            builder += when (types.size) {
                1 -> "types: ${types[0].name}"
                else -> "types: [${types.joinToString { it.name }}]"
            }
        }
        //        }
    }

    //    operator fun invoke(block: WebhookEventInterface<T>.() -> Unit) {
    //        indent {
    //            builder += "$name:"
    //            indent {
    //                builder += "types:"
    //                block()
    //            }
    //        }
    //    }
}

interface WebhookEvents {

    val checkRun
        get() = WebhookEvent.checkRun.apply {
            builder += "$name:"
            skipEventName = true
        }
    val checkSuite
        get() = WebhookEvent.checkSuite.apply {
            builder += "$name:"
            skipEventName = true
        }
    val create
        get() = WebhookEvent.create.apply {
            builder += "$name:"
            skipEventName = true
        }
    val delete
        get() = WebhookEvent.delete.apply {
            builder += "$name:"
            skipEventName = true
        }
    val deployment
        get() = WebhookEvent.deployment.apply {
            builder += "$name:"
            skipEventName = true
        }
    val deploymentStatus
        get() = WebhookEvent.deploymentStatus.apply {
            builder += "$name:"
            skipEventName = true
        }
    val discussion
        get() = WebhookEvent.discussion.apply {
            builder += "$name:"
            skipEventName = true
        }
    val discussionComment
        get() = WebhookEvent.discussionComment.apply {
            builder += "$name:"
            skipEventName = true
        }
    val fork
        get() = WebhookEvent.fork.apply {
            builder += "$name:"
            skipEventName = true
        }
    val gollum
        get() = WebhookEvent.gollum.apply {
            builder += "$name:"
            skipEventName = true
        }
    val issueComment
        get() = WebhookEvent.issueComment.apply {
            builder += "$name:"
            skipEventName = true
        }
    val issues
        get() = WebhookEvent.issues.apply {
            builder += "$name:"
            skipEventName = true
        }
    val label
        get() = WebhookEvent.label.apply {
            builder += "$name:"
            skipEventName = true
        }
    val milestone
        get() = WebhookEvent.milestone.apply {
            builder += "$name:"
            skipEventName = true
        }
    val pageBuild
        get() = WebhookEvent.pageBuild.apply {
            builder += "$name:"
            skipEventName = true
        }
    val project
        get() = WebhookEvent.project.apply {
            builder += "$name:"
            skipEventName = true
        }
    val projectCard
        get() = WebhookEvent.projectCard.apply {
            builder += "$name:"
            skipEventName = true
        }
    val projectColumn
        get() = WebhookEvent.projectColumn.apply {
            builder += "$name:"
            skipEventName = true
        }
    val public
        get() = WebhookEvent.public.apply {
            builder += "$name:"
            skipEventName = true
        }
    val pullRequest
        get() = WebhookEvent.pullRequest.apply {
            builder += "$name:"
            skipEventName = true
        }
    val pullRequestReview
        get() = WebhookEvent.pullRequestReview.apply {
            builder += "$name:"
            skipEventName = true
        }
    val pullRequestReviewComment
        get() = WebhookEvent.pullRequestReviewComment.apply {
            builder += "$name:"
            skipEventName = true
        }
    val pullRequestTarget
        get() = WebhookEvent.pullRequestTarget.apply {
            builder += "$name:"
            skipEventName = true
        }
    val push
        get() = WebhookEvent.push.apply {
            builder += "$name:"
            skipEventName = true
        }
    val registryPackage
        get() = WebhookEvent.registryPackage.apply {
            builder += "$name:"
            skipEventName = true
        }
    val release
        get() = WebhookEvent.Release.apply {
            builder += "$name:"
            skipEventName = true
        }
    val status
        get() = WebhookEvent.status.apply {
            builder += "$name:"
            skipEventName = true
        }
    val watch
        get() = WebhookEvent.watch.apply {
            builder += "$name:"
            skipEventName = true
        }
    val workflowRun
        get() = WebhookEvent.workflowRun.apply {
            builder += "$name:"
            skipEventName = true
        }
}

object WebhookEvent {


    operator fun Enum<*>.invoke(): Enum<*> = apply { builder += "- $name" }

    object checkRun : WebhookEventInterface<checkRun> {
        override val name: String get() = "check_run"

        val create get() = Type.create()
        val rerequested get() = Type.rerequested()
        val completed get() = Type.completed()

        enum class Type : WebhookEventType<checkRun> { create, rerequested, completed }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object checkSuite : WebhookEventInterface<checkSuite> {
        override val name: String get() = "check_suite"

        val completed = Type.completed()
        val requested = Type.requested()
        val rerequested = Type.rerequested()

        enum class Type : WebhookEventType<checkSuite> { completed, requested, rerequested }

        operator fun invoke(vararg types: Type) = types(*types)
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

        val created get() = Type.created()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()
        val transferred get() = Type.transferred()
        val pinned get() = Type.pinned()
        val unpinned get() = Type.unpinned()
        val labeled get() = Type.labeled()
        val unlabeled get() = Type.unlabeled()
        val locked get() = Type.locked()
        val unlocked get() = Type.unlocked()
        val category_changed get() = Type.category_changed()
        val answered get() = Type.answered()
        val unanswered get() = Type.unanswered()

        enum class Type : WebhookEventType<discussion> { created, edited, deleted, transferred, pinned, unpinned, labeled, unlabeled, locked, unlocked, category_changed, answered, unanswered }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object discussionComment : WebhookEventInterface<discussionComment> {
        override val name: String get() = "discussion_comment"

        val created get() = Type.created()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<discussionComment> { created, edited, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object fork : WebhookEventInterface<fork> {
        override val name: String get() = "fork"
    }

    object gollum : WebhookEventInterface<gollum> {
        override val name: String get() = "gollum"
    }

    object issueComment : WebhookEventInterface<issueComment> {
        override val name: String get() = "issue_comment"

        val created get() = Type.created()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<issueComment> { created, edited, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object issues : WebhookEventInterface<issues> {
        override val name: String get() = "issues"


        val opened get() = Type.opened()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()
        val transferred get() = Type.transferred()
        val pinned get() = Type.pinned()
        val unpinned get() = Type.unpinned()
        val closed get() = Type.closed()
        val reopened get() = Type.reopened()
        val assigned get() = Type.assigned()
        val unassigned get() = Type.unassigned()
        val labeled get() = Type.labeled()
        val unlabeled get() = Type.unlabeled()
        val locked get() = Type.locked()
        val unlocked get() = Type.unlocked()
        val milestoned get() = Type.milestoned()
        val demilestoned get() = Type.demilestoned()

        enum class Type : WebhookEventType<issues> { opened, edited, deleted, transferred, pinned, unpinned, closed, reopened, assigned, unassigned, labeled, unlabeled, locked, unlocked, milestoned, demilestoned }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object label : WebhookEventInterface<label> {
        override val name: String get() = "label"
        val created get() = Type.created()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<label> { created, edited, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object milestone : WebhookEventInterface<milestone> {
        override val name: String get() = "milestone"

        val created get() = Type.created()
        val closed get() = Type.closed()
        val opened get() = Type.opened()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<milestone> { created, closed, opened, edited, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object pageBuild : WebhookEventInterface<pageBuild> {
        override val name: String get() = "page_build"
    }

    object project : WebhookEventInterface<project> {
        override val name: String get() = "project"

        val created get() = Type.created()
        val updated get() = Type.updated()
        val closed get() = Type.closed()
        val reopened get() = Type.reopened()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<project> { created, updated, closed, reopened, edited, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object projectCard : WebhookEventInterface<projectCard> {
        override val name: String get() = "project_card"

        val created get() = Type.created()
        val moved get() = Type.moved()
        val converted get() = Type.converted()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<projectCard> { created, moved, converted, edited, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object projectColumn : WebhookEventInterface<projectColumn> {
        override val name: String get() = "project_column"

        val created get() = Type.created()
        val updated get() = Type.updated()
        val moved get() = Type.moved()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<projectColumn> { created, updated, moved, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object public : WebhookEventInterface<public> {
        override val name: String get() = "public"
    }

    object pullRequest : PushPullRequestFeatures<pullRequest> {
        override val name: String get() = "pull_request"

        val assigned get() = Type.assigned()
        val unassigned get() = Type.unassigned()
        val labeled get() = Type.labeled()
        val unlabeled get() = Type.unlabeled()
        val opened get() = Type.opened()
        val edited get() = Type.edited()
        val closed get() = Type.closed()
        val reopened get() = Type.reopened()
        val synchronize get() = Type.synchronize()
        val converted_to_draft get() = Type.converted_to_draft
        val ready_for_review get() = Type.ready_for_review
        val locked get() = Type.locked()
        val unlocked get() = Type.unlocked()
        val review_requested get() = Type.review_requested()
        val review_request_removed get() = Type.review_request_removed()
        val auto_merge_enabled get() = Type.auto_merge_enabled()
        val auto_merge_disabled get() = Type.auto_merge_disabled()

        enum class Type : WebhookEventType<pullRequest> { assigned, unassigned, labeled, unlabeled, opened, edited, closed, reopened, synchronize, converted_to_draft, ready_for_review, locked, unlocked, review_requested, review_request_removed, auto_merge_enabled, auto_merge_disabled }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object pullRequestReview : WebhookEventInterface<pullRequestReview> {
        override val name: String get() = "pull_request_review"

        val submitted get() = Type.submitted()
        val edited get() = Type.edited()
        val dismissed get() = Type.dismissed()

        enum class Type : WebhookEventType<pullRequestReview> { submitted, edited, dismissed }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object pullRequestReviewComment : WebhookEventInterface<pullRequestReviewComment> {
        override val name: String get() = "pull_request_review_comment"

        val created get() = Type.created()
        val edited get() = Type.edited()
        val deleted get() = Type.deleted()

        enum class Type : WebhookEventType<pullRequestReviewComment> { created, edited, deleted }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object pullRequestTarget : WebhookEventInterface<pullRequestTarget> {
        override val name: String get() = "pull_request_target"

        val assigned get() = Type.assigned()
        val unassigned get() = Type.unassigned()
        val labeled get() = Type.labeled()
        val unlabeled get() = Type.unlabeled()
        val opened get() = Type.opened()
        val edited get() = Type.edited()
        val closed get() = Type.closed()
        val reopened get() = Type.reopened()
        val synchronize get() = Type.synchronize()
        val converted_to_draft get() = Type.converted_to_draft()
        val ready_for_review get() = Type.ready_for_review()
        val locked get() = Type.locked()
        val unlocked get() = Type.unlocked()
        val review_requested get() = Type.review_requested()
        val review_request_removed get() = Type.review_request_removed()
        val auto_merge_enabled get() = Type.auto_merge_enabled()
        val auto_merge_disabled get() = Type.auto_merge_disabled()

        enum class Type : WebhookEventType<pullRequestTarget> { assigned, unassigned, labeled, unlabeled, opened, edited, closed, reopened, synchronize, converted_to_draft, ready_for_review, locked, unlocked, review_requested, review_request_removed, auto_merge_enabled, auto_merge_disabled }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object push : PushPullRequestFeatures<push> {
        override val name: String get() = "push"
    }

    object registryPackage : WebhookEventInterface<registryPackage> {
        override val name: String get() = "registry_package"

        enum class Type : WebhookEventType<registryPackage> { published, updated }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object Release : WebhookEventInterface<Release> {
        override val name: String get() = "release"

        val created get() = Type.created()

        enum class Type : WebhookEventType<Release> { published, unpublished, created, edited, deleted, prereleased, released }

        operator fun invoke(vararg types: Type) = types(*types)
        operator fun invoke(block: Release.() -> Unit) {
            indent {
                builder += "types:"
                indent {
                    Release.block()
                }
            }
        }
    }

    object status : WebhookEventInterface<status> {
        override val name: String get() = "status"
    }

    object watch : WebhookEventInterface<watch> {
        override val name: String get() = "watch"

        enum class Type : WebhookEventType<watch> { started }

        operator fun invoke(vararg types: Type) = types(*types)
    }

    object workflowRun : WebhookEventInterface<workflowRun> {
        override val name: String get() = "workflow_run"

        enum class Type : WebhookEventType<workflowRun> { completed, requested }

        operator fun invoke(vararg types: Type) = types(*types)
    }
}

internal var skipEventName = false

interface PushPullRequestFeatures<T> : WebhookEventInterface<T> {

    operator fun invoke(block: PushPullRequestFeatures<T>.() -> Unit) {
        indent {
            if (skipEventName)
                skipEventName = false
//            else
//                builder += "$name:"
            block()
        }
    }

    fun func(name: String, block: Builder.() -> Unit) {
        builder += "$name:"
        indent {
            Builder.block()
        }
    }

    fun func(name: String, vararg branches: String) {
        builder += "$name:"
        indent {
            for (branch in branches)
                builder += when {
                    branch.singleQuote -> "- '$branch'"
                    else -> "- $branch"
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
        operator fun String.unaryPlus() {
            builder += when {
                singleQuote -> "- '$this'"
                else -> "- $this"
            }
        }

        operator fun String.unaryMinus() {
            builder += when {
                singleQuote -> "- '!$this'"
                else -> "- !$this"
            }
        }
    }
}