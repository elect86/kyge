package kyge.actions

import kyge.*
import kyge.builder

object checkout {

    val v2 = V2

    object V2 : Step(), Action<V2.Builder> {

        override val actionName = "checkout"
        override val actionVersion = "v2"
        override val t = Builder()

        class Builder {

            /** Repository name with owner. For example, actions/checkout
             *  Default: github.repository */
            var repository: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "repository: $value"
                }

            /** The branch, tag or SHA to checkout. When checking out the repository that triggered a workflow, this
             *  defaults to the reference or SHA for that event. Otherwise, uses the default branch. */
            var ref: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "ref: $value"
                }

            /** Personal access token (PAT) used to fetch the repository. The PAT is configured with the local git
             *  config, which enables your scripts to run authenticated git commands. The post-job step removes the PAT.
             *
             *  We recommend using a service account with the least permissions necessary. Also when generating a new
             *  PAT, select the least scopes necessary.
             *
             *  [Learn more about creating and using encrypted secrets](https://help.github.com/en/actions/automating-your-workflow-with-github-actions/creating-and-using-encrypted-secrets)
             *
             *  Default: github.token */
            var token: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "token: $value"
                }

            /** SSH key used to fetch the repository. The SSH key is configured with the local git config, which enables
             *  your scripts to run authenticated git commands. The post-job step removes the SSH key.
             *
             *  We recommend using a service account with the least permissions necessary.
             *
             *  [Learn more about creating and using encrypted secrets](https://help.github.com/en/actions/automating-your-workflow-with-github-actions/creating-and-using-encrypted-secrets) */
            var sshKey: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "ssh-key: $value"
                }

            /** Known hosts in addition to the user and global host key database. The public SSH keys for a host may be
             *  obtained using the utility `ssh-keyscan`. For example, `ssh-keyscan github.com`. The public key for
             *  github.com is always implicitly added. */
            var sshKnownHosts: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "ssh-known-hosts: $value"
                }

            /** Whether to perform strict host key checking. When true, adds the options `StrictHostKeyChecking=yes` and
             *  `CheckHostIP=no` to the SSH command line. Use the input `ssh-known-hosts` to configure additional hosts.
             *  Default: true */
            var sshStrict: Boolean
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "ssh-strict: $value"
                }

            /** Whether to configure the token or SSH key with the local git config
             *  Default: true */
            var persistCredentials: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "persist-credentials: $value"
                }

            /** Relative path under $GITHUB_WORKSPACE to place the repository */
            var path: String
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "path: $value"
                }

            /** Whether to execute `git clean -ffdx && git reset --hard HEAD` before fetching
             *  Default: true */
            var clean: Boolean
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "clean: $value"
                }

            /** Number of commits to fetch. 0 indicates all history for all branches and tags.
             *  Default: 1 */
            var fetchDepth: Int
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "fetch-depth: $value"
                }

            /** [Kyge] custom */
            val fetchAll: Unit
                get() {
                    fetchDepth = 0
                }

            /** Whether to download Git-LFS files
             *  Default: false */
            var lfs: Boolean
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "lfs: $value"
                }

            /** Whether to checkout submodules: `true` to checkout submodules or `recursive` to recursively checkout
             *  submodules.
             *
             *  When the `ssh-key` input is not provided, SSH URLs beginning with `git@github.com:` are converted to
             *  HTTPS.
             *
             *  Default: false */
            var submodules: Boolean
                @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
                set(value) {
                    builder += "submodules: $value"
                }
        }
    }
}