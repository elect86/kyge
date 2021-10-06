package kyge


interface OptionsInterface {

    var options: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            builder += "options: $value"
        }

    fun options(block: Options.() -> Unit) {
        val options = Options().also(block)
        builder += "options: $options"
    }
}

@KygeMarker
class Options {

    private val build = StringBuilder()
    private fun add(string: String) {
        build.append(string)
    }

    /** Add a custom host-to-IP mapping (host:ip) */
    var addHost: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--add-host $value")

    /** Attach to STDIN, STDOUT or STDERR */
    var attach: Attach
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-a $value")

    enum class Attach { STDIN, STDOUT, STDERR }

    /** Block IO (relative weight), between 10 and 1000, or 0 to disable (default 0) */
    var blkioWeight: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            check(value in 10..1000 || value == 0)
            add("--blkio-weight $value")
        }

    /** Block IO weight (relative device weight) */
    var blkioWeightDevice: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--blkio-weight-device $value")

    /** Add or drop Linux capabilities */
    var linuxCap: Boolean
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add(when {
                             value -> "--cap-add"
                             else -> "--cap-drop"
                         })

    /** Cgroup namespace to use */
    var cGroupNamespace: Namespace
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cgroupns $value")

    enum class Namespace {
        /** Run the container in the Docker host's cgroup namespace */
        host,

        /** Run the container in its own private cgroup namespace */

        private,

        /** Use the cgroup namespace as configured by the default-cgroupns-mode option on the daemon (default) */

        default;

        override fun toString() = when (this) {
            default -> ""
            else -> name
        }
    }

    /** Write the container ID to the file */
    var cidfile: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cidfile $value")

    /** CPU count (Windows only) */
    var cpuCount: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cpu-count $value")

    /** CPU percent (Windows only) */
    var cpuPercent: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cpu-percent $value")

    /** Limit CPU CFS (Completely Fair Scheduler) period */
    var cpuPeriod: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cpu-period $value")

    /** Limit CPU CFS (Completely Fair Scheduler) quota */
    var cpuQuote: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cpu-quote $value")

    /** Limit CPU real-time period in microseconds */
    var cpuRtPeriod: MicroSeconds
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cpu-rt-period $value")

    /** Limit CPU real-time runtime in microseconds */
    var cpuRtRuntime: MicroSeconds
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cpu-rt-runtime $value")

    /** CPU shares (relative weight) */
    var cpuShares: MicroSeconds
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-c $value")

    /** Number of CPUs */
    var cpus: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--cpus $value")

    /** CPUs in which to allow execution (0-3, 0,1) */
    fun cpusetCpus(value: Int) = add("--cpuset-cpus $value")

    /** CPUs in which to allow execution (0-3, 0,1) */
    fun cpusetCpus(value: IntRange) = add("--cpuset-cpus ${value.first}-${value.last}")

    /** MEMs in which to allow execution (0-3, 0,1) */
    fun cpusetMems(value: Int) = add("--cpuset-mems $value")

    /** MEMs in which to allow execution (0-3, 0,1) */
    fun cpusetMems(value: IntRange) = add("--cpuset-mems ${value.first}-${value.last}")

    /** Add a host device to the container */
    var device: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--device $value")

    /** Add a rule to the cgroup allowed devices list */
    var deviceCgroupRule: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--device-cgroup-rule $value")

    /** Limit read rate (bytes per second) from a device */
    var deviceReadBPS: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--device-read-bps $value")

    /** Limit read rate (IO per second) from a device */
    var deviceReadIOPS: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--device-read-iops $value")

    /** Limit write rate (bytes per second) to a device */
    var deviceWriteBPS: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--device-write-bps $value")

    /** Limit write rate (IO per second) to a device */
    var deviceWriteIOPS: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--device-write-iops $value")

    /** Skip image verification */
    var disableContentTrust: Boolean
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--disable-content-trust $value")

    /** Set custom DNS servers */
    var dns: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--dns $value")

    /** Set DNS options */
    var dnsOpt: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--dns-opt $value")

    /** Set DNS options */
    var dnsOptions: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--dns-option $value")

    /** Set custom DNS search domains */
    var dnsSearch: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--dns-search $value")

    /** Container NIS domain name */
    var domainName: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--domainname $value")

    /** Overwrite the default ENTRYPOINT of the image */
    var entryPoint: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--entrypoint $value")

    /** Set environment variables */
    var env: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-e $value")

    /** Set environment variables */
    var envFile: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--env-file $value")

    /** Expose a port or a range of ports */
    var expose: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--expose $value")

    /** GPU devices to add to the container ('all' to pass all GPUs) */
    var gpus: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--gpus $value")

    /** GPU devices to add to the container ('all' to pass all GPUs) */
    var groupAdd: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--group-add $value")

    /** Command to run to check health */
    var healthCmd: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--health-cmd $value")

    /** Time between running the check (ms|s|m|h) (default 0s) */
    var healthInterval: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--health-interval $value")

    /** Consecutive failures needed to report unhealthy */
    var healthRetries: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--health-retries $value")

    /** Start period for the container to initialize before starting health-retries countdown (ms|s|m|h) (default 0s) */
    var healthStartPeriod: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--health-start-period $value")

    /** Maximum time to allow one check to run (ms|s|m|h) (default 0s) */
    var healthTimeout: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--health-timeout $value")

    /** Print usage */
    val help: Unit
        get() = add("--help")

    /** Container host name */
    var hostname: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-h $value")

    /** Run an init inside the container that forwards signals and reaps processes */
    val init: Unit
        get() = add("--init")

    /** Keep STDIN open even if not attached */
    val interactive: Unit
        get() = add("-i")

    /** Maximum IO bandwidth limit for the system drive (Windows only) */
    var ioMaxBandwidth: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--io-maxbandwidth $value")

    /** Maximum IOps limit for the system drive (Windows only) */
    var ioMaxIOPS: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--io-maxiops $value")

    /** IPv4 address (e.g., 172.30.100.104) */
    var ip: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--ip $value")

    /** IPv6 address (e.g., 2001:db8::33) */
    var ip6: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--ip6 $value")

    /** IPC mode to use */
    var ipc: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--ipc $value")

    /** Container isolation technology */
    var isolation: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--isolation $value")

    /** Kernel memory limit */
    var kernelMemory: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--kernel-memory $value")

    /** Set meta data on a container */
    var label: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-l $value")

    /** Read in a line delimited file of labels */
    var labelFile: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--label-file $value")

    /** Add link to another container */
    var link: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--link $value")

    /** Container IPv4/IPv6 link-local addresses */
    var linkLocalIP: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--link $value")

    /** Logging driver for the container */
    var logDriver: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--log-driver $value")

    /** Logging driver for the container */
    var logOpt: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--log-opt $value")

    /** Container MAC address (e.g., 92:d0:c6:0a:29:33) */
    var macAddress: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--mac-address $value")

    /** Memory limit */
    var memoryLimit: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-m $value")

    /** Memory soft limit */
    var memorySoftLimit: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--memory-reservation $value")

    /** Swap limit equal to memory plus swap: '-1' to enable unlimited swap */
    var memorySwap: Int
        get() {
            add("--memory-swap")
            return -1
        }
        set(value) = add("--memory-swap $value")

    /** Tune container memory swappiness (0 to 100) */
    var memorySwappiness: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            check(value in 0..100)
            add("--memory-swappiness $value")
        }

    /** Attach a filesystem mount to the container */
    var mount: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--mount $value")

    /** Assign a name to the container */
    var name: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--name $value")

    /** Connect a container to a network */
    var net: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--net $value")

    /** Add network-scoped alias for the container */
    var netAlias: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--net-alias $value")

    //   Warning: The --network option is not supported.

    //    /** Connect a container to a network */
    //    var network: String
    //        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
    //        set(value) = add("--network $value")

    /** Add network-scoped alias for the container */
    var networkAlias: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--network-alias $value")

    /** Disable any container-specified HEALTHCHECK */
    val noHealthCheck: Unit
        get() = add("--no-healthcheck")

    /** Disable OOM Killer */
    val oomKillDisable: Unit
        get() = add("--oom-kill-disable")

    /** Tune host's OOM preferences (-1000 to 1000) */
    var oomScoreAdj: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) {
            check(value in 1000..1000)
            add("--oom-score-adj $value")
        }

    /** PID namespace to use */
    var pid: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--pid $value")

    /** Tune container pids limit (set -1 for unlimited) */
    var pidsLimit: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--pids-limit $value")

    /** Set platform if server is multi-platform capable */
    var platform: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--platform $value")

    /** Give extended privileges to this container */
    var privileged: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--privileged $value")

    /** Publish a container's port(s) to the host */
    var publish: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-p $value")

    /** Publish all exposed ports to random ports */
    var publishAll: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-P $value")

    /** Pull image before creating ("always"|"missing"|"never") */
    var pull: Pull
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--pull $value")

    /** Mount the container's root filesystem as read only */
    var readOnly: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--read-only $value")

    /** Restart policy to apply when a container exits */
    var restart: Boolean
        get() {
            add("--restart yes")
            return true
        }
        set(value) = add("--restart ${if (value) "yes" else "no"}")

    /** Automatically remove the container when it exits */
    var rm: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--rm $value")

    /** Runtime to use for this container */
    var runtime: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--runtime $value")

    /** Security Options */
    var securityOpt: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--security-opt $value")

    /** Size of /dev/shm */
    var shmSize: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--shm-size $value")

    /** Signal to stop a container */
    var stopSignal: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--stop-signal $value")

    /** Timeout (in seconds) to stop a container */
    var stopTimeout: Int
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--stop-timeout $value")

    /** Storage driver options for the container */
    var stopOpt: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--stop-opt $value")

    /** Sysctl options */
    var sysctl: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--sysctl $value")

    /** Mount a tmpfs directory */
    var tmpfs: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--tmpfs $value")

    /** Allocate a pseudo-TTY */
    val tty: Unit
        get() = add("-t")

    /** Ulimit options */
    var ulimit: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--ulimit $value")

    /** Username or UID (format: <name|uid>[:<group|gid>]) */
    var user: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-u $value")

    /** User namespace to use */
    var userns: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--userns $value")

    /** UTS namespace to use */
    var uts: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--uts $value")

    /** Bind mount a volume */
    var volume: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-v $value")

    /** Optional volume driver for the container */
    var volumeDriver: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--volume-driver $value")

    /** Mount volumes from the specified container(s) */
    var volumeFrom: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("--volume-from $value")

    /** Working directory inside the container */
    var workDir: String
        @Deprecated(message = "Write only property", level = DeprecationLevel.HIDDEN) get() = error("")
        set(value) = add("-w $value")

    enum class Pull { always, missing, never }

    override fun toString() = build.toString()
}