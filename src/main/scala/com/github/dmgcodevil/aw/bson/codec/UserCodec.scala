package com.github.dmgcodevil.aw.bson.codec

import com.github.dmgcodevil.aw.entity.User
import org.bson.codecs.{Codec, DecoderContext, EncoderContext}
import org.bson.{BsonReader, BsonWriter}

/**
  * Created by dmgcodevil on 1/26/2018.
  */
class UserCodec extends Codec[User] {

  override def decode(reader: BsonReader, decoderContext: DecoderContext): User = {
    reader.readStartDocument()
    val str = User(reader.readObjectId(), reader.readString("name"), reader.readString("email"))
    reader.readEndDocument()
    str
  }

  override def encode(writer: BsonWriter, value: User, encoderContext: EncoderContext): Unit = {
    writer.writeStartDocument()
    writer.writeObjectId("_id", value.id)
    writer.writeString("name", value.name)
    writer.writeString("email", value.email)
    writer.writeEndDocument()
  }

  override def getEncoderClass: Class[User] = classOf[User]
}