package shinobi9.krcon.protocol

import java.nio.charset.Charset


/**
 *```
 *   ____________________________________________________________________________
 *  |    Field      |        Type                            |       Value       |
 *  |---------------|----------------------------------------|-------------------|
 *  |    Size       |  32-bit little-endian Signed Integer   | Varies, see below.|
 *  |     ID        |  32-bit little-endian Signed Integer   | Varies, see below.|
 *  |    Type       |  32-bit little-endian Signed Integer   | Varies, see below.|
 *  |    Body       |     Null-terminated ASCII String       | Varies, see below.|
 *  |  Empty String |     Null-terminated ASCII String       |      0x00         |
 *  ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯
 *```
 * see [valve's docs](https://developer.valvesoftware.com/wiki/Source_RCON_Protocol)
 */
class RCONPacket {
    var size: Int = 0
    var id: Int = 0
    var type: Int = 0
    var body: ByteArray = byteArrayOf(0x00)
    var emptyString: Byte = 0x00

    internal constructor()
    internal constructor(payload: String, packetType: PacketType, generateId: Int, encodeCharset: Charset) {
        val bytes = payload.toByteArray(encodeCharset)
        size = bytes.size + 10
        id = generateId
        type = packetType.value
        body = bytes + body
    }

    enum class PacketType(val value: Int) {
        SERVERDATA_AUTH(3),
        SERVERDATA_AUTH_RESPONSE(2),
        SERVERDATA_EXECCOMMAND(2),
        SERVERDATA_RESPONSE_VALUE(0)
    }
}