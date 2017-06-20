package nl.molnet.app

import com.arangodb.ArangoDBAsync
import com.arangodb.ArangoDBException
import com.arangodb.ArangoDatabaseAsync

fun main(args: Array<String>) {
    println("Start app...")
    var db = initArangoDb()

    val carsBatch: MutableList<Car> = mutableListOf()
    for (i in 1 until 100001) {
        val car1 = Car("Toyota" + i)
        carsBatch.add(car1)
        if (i % 500 == 0) {
            insertBatch(db, carsBatch)
            carsBatch.clear()
        }
    }

    println("Finished app...")

    try {
        Thread.sleep(10000)
    } finally {
        println("Stop app...")
    }
}

fun insertBatch(db: ArangoDatabaseAsync, batch: MutableList<Car>) {
    val collection = db.collection("cars").insertDocuments(batch)

}

fun initArangoDb(): ArangoDatabaseAsync {
    var adminUser = "admin"
    var dbName = "ReactiveTest";

    val arangoDb = ArangoDBAsync.Builder()
            //.registerDeserializer(ZonedDateTime::class.java, CustomArangoDeSerializers.DESERIALIZE_ZONED_DATE_TIME)
            //.registerSerializer(ZonedDateTime::class.java, CustomArangoDeSerializers.SERIALIZE_ZONED_DATE_TIME)
            .build()

    if (arangoDb.getUser(adminUser) == null) {
        arangoDb.createUser(adminUser, adminUser)
    }

    try {
        try {
            arangoDb.createDatabase(dbName)
        } catch(e: Exception) {
            println("db already exists...")
        }

        val db = arangoDb.db(dbName)
        db.grantAccess(adminUser)

        try {
            db.createCollection("cars", null)
        } catch(e: Exception) {
            println("collection already exists...")
        }

        return db
    } catch (e: ArangoDBException) {
        // todo do some logging
        //logger.error("Error initializing ArangoDb", e)
    }

    throw RuntimeException("Error creating db " + dbName)
}
