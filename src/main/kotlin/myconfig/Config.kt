package bsync.myconfig

import org.apache.tuweni.toml.*


object Config {

    enum class KeyPaths (val path: String) {
        PLAID_ENVIRONMENT("plaid.environment"),
        PLAID_CLIENTID("plaid.$.clientId"),
        PLAID_SECRETKEY("plaid.$.secretKey"),
        PLAID_PUBKEY("plaid.$.publicKey")
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
        val fullKey = "config." + environmentPath(keypath.path)
        return KeyVal(value = getStrLower(fullKey), path = fullKey)
    }

    val str = fun(getter: () -> KeyVal) : String {
        val keyvalue = getter()
        return keyvalue.value ?: throw NoSuchFieldException("Configuration not found for path: ${keyvalue.path}")
    }

    val environmentPath = fun(path: String?) : String {
        if (path == null) return ""
        // Just return path with no setting of environment.
        val env = getStrLower(KeyPaths.PLAID_ENVIRONMENT.path) ?: return path
        // Substitute environment portion of the path.
        return path.replace(".$.", ".${env}.")
    }
}

fun loadConfig(text: String) {
    Config(text)
}


//fun verify(keypath: KeyPaths, getter: (keypath: KeyPaths) -> String?) : String {
//    val value = getter(keypath)
//    return value ?: throw NoSuchFieldException("Configuration not found for path: ${keypath.path}")
//}
