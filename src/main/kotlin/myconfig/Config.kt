package bsync.myconfig

import org.apache.tuweni.toml.*


object Config {

    enum class KeyPaths (val path: String, val envUsed: String) {
        PLAID_ENVIRONMENT("plaid.environment", ""),
        PLAID_CLIENTID("plaid.$.clientId", "plaid_environment"),
        PLAID_SECRETKEY("plaid.$.secretKey", "plaid_environment"),
        PLAID_PUBKEY("plaid.$.publicKey", "plaid_environment")
    }

    data class KeyVal (val value: String?, val path: String)

    private lateinit var config: TomlParseResult


    operator fun invoke(text: String) : Config {
        config = Toml.parse(text)
        return this
    }

    fun getStrLower(key: String) : String? {
        val value = config.getString(key)
        return value?.toLowerCase() ?: value
    }

    val configValue = fun (keypath: KeyPaths) : KeyVal {
        // Fill in environment piece of key path.
        val fullKey = "config." + environmentPath(keypath.path, keypath.envUsed)
        return KeyVal(value = getStrLower(fullKey), path = fullKey)
    }

    val str = fun(getter: () -> KeyVal) : String {
        val keyvalue = getter()
        return keyvalue.value ?: throw NoSuchFieldException("Configuration not found for path: ${keyvalue.path}")
    }

    val environmentPath = fun(path: String?, envUsed: String) : String {
        if (path == null) return ""
        // If no environment just return key path with no setting of environment.
        val env = if (envUsed == "plaid_environment") {
            getStrLower(KeyPaths.PLAID_ENVIRONMENT.path) ?: return path
        }
        else {
            envUsed
        }
        // Substitute environment portion of the path.
        return path.replace(".$.", ".${env}.")
    }
}

fun loadConfig(text: String) {
    Config(text)
}
