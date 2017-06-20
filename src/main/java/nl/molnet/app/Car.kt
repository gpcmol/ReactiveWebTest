package nl.molnet.app

import com.arangodb.entity.DocumentField

class Car(val brand: String){
    @DocumentField(DocumentField.Type.KEY)
    private val key: String? = null
    var subBrand: String = ""
    var volume: String = ""
    var valves: String = ""
    var color: String = ""
}

