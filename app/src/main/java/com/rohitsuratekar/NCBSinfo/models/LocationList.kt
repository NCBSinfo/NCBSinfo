package com.rohitsuratekar.NCBSinfo.models

import java.util.*


private fun listHalls(): ArrayList<Array<String>> {
    val lectureList = ArrayList<Array<String>>()
    lectureList.add(
        arrayOf(
            "Annex",
            "Meeting Room",
            "Admin Block",
            "Second Floor",
            "Director's Office",
            "Meeting Room"
        )
    )
    lectureList.add(
        arrayOf(
            "Axon",
            "VC Room",
            "Admin Block",
            "Second Floor",
            "Inside Dean's Office",
            "Video Confenerce Room"
        )
    )
    lectureList.add(
        arrayOf(
            "Banganapalli",
            "Old Teaching Lab",
            "Admin Block",
            "Basement",
            "Below Account Section",
            "Teaching Lab"
        )
    )
    lectureList.add(
        arrayOf(
            "Bleb",
            "GF Meeting Room",
            "Admin Block",
            "Ground Floor",
            "In front of Account Section",
            "Meeting Room"
        )
    )
    lectureList.add(
        arrayOf(
            "Centriole ",
            "CCAMP Meeting Room",
            "CCAMP Building",
            "First Floor",
            "CCAMP Building",
            "Meeting Room"
        )
    )
    lectureList.add(
        arrayOf(
            "Chausa",
            "CCAMP Seminar Hall",
            "CCAMP Building",
            "Ground Floor",
            "CCAMP Building",
            "Seminar Hall"
        )
    )
    lectureList.add(
        arrayOf(
            "Chloroplast",
            "S-03 Seminar Hall",
            "Eastern Labs",
            "Second Floor",
            "Next to Sowdhamini's Lab",
            "Seminar Hall"
        )
    )
    lectureList.add(
        arrayOf(
            "Cilium",
            "FF Meeting Room",
            "Admin Block",
            "First Floor",
            "Next to Architect office",
            "Meeting Room"
        )
    )
    lectureList.add(
        arrayOf(
            "Dasheri",
            "200 Seater",
            "Southern Labs",
            "Ground Floor",
            "Towards Reception",
            "Auditorium"
        )
    )
    lectureList.add(
        arrayOf(
            "Endosome",
            "SLCFF Seminar Hall",
            "Southern Labs",
            "First Floor",
            "Towards Canteen",
            "Seminar Hall"
        )
    )
    lectureList.add(
        arrayOf(
            "Faculty Lounge",
            "Faculty Lounge",
            "Southern Labs",
            "Second Floor",
            "Towards Reception",
            "Meeting Room"
        )
    )
    lectureList.add(
        arrayOf(
            "Golgi",
            "S-02 Seminar Hall",
            "Eastern Labs",
            "First Floor",
            "In front of Jayant's Lab",
            "Seminar Hall"
        )
    )
    lectureList.add(arrayOf("Haapus ", "LH-1", "Eastern Labs", "Ground Floor", "In front of reception", "Lecture Hall"))
    lectureList.add(
        arrayOf(
            "Himsagar",
            "LH-3",
            "Eastern Labs",
            "Second Floor",
            "In front of Wildlife office",
            "Lecture Hall"
        )
    )
    lectureList.add(arrayOf("InStem Auditorium", "100 Seater", "InStem Building", "Ground Floor", "Towards InStem-Canteen", "Auditorium"))
    lectureList.add(arrayOf("Langra", "LH-2", "Eastern Labs", "First Floor", "Above reception", "Lecture Hall"))
    lectureList.add(arrayOf("Malgova", "70 Seater", "Southern Labs", "Second Floor", "Next to Library", "Auditorium"))
    lectureList.add(
        arrayOf(
            "Meeting room (Admin) ",
            "Meeting room (Admin) ",
            "Admin Block",
            "Third Floor",
            "RDO",
            "Meeting Room"
        )
    )
    lectureList.add(
        arrayOf(
            "Mitochondrion",
            "SLCSF Seminal Hall",
            "Southern Labs",
            "Second Floor",
            "Towards Canteen",
            "Seminar Hall"
        )
    )
    lectureList.add(
        arrayOf(
            "Nucleus",
            "S-01 Seminar Hall",
            "Eastern Labs",
            "Ground Floor",
            "Next to Lab Support",
            "Seminar Hall"
        )
    )
    lectureList.add(
        arrayOf(
            "Plasmid",
            "15 seater",
            "Eastern Labs",
            "Second Floor",
            "Near Fly Facility",
            "Open Air Meeting Space"
        )
    )
    lectureList.add(
        arrayOf(
            "Raspuri",
            "New Teaching Lab",
            "Southern Labs",
            "Ground Floor",
            "Next to tennis court",
            "Teaching Lab"
        )
    )
    lectureList.add(arrayOf("Ribosome", "GF Seminar Hall", "CCAMP Building", "Ground Floor", "N/A", "Seminar Hall"))
    lectureList.add(arrayOf("Safeda", "40 Seater", "Southern Labs", "Second Floor", "Towards Canteen", "Auditorium"))
    lectureList.add(
        arrayOf(
            "Synapse",
            "SLCGF Seminar Hall",
            "Southern Labs",
            "Ground Floor",
            "Towards Canteen",
            "Seminar Hall"
        )
    )
    lectureList.add(
        arrayOf(
            "Vacuole",
            "15 seater",
            "Eastern Labs",
            "Second Floor",
            "Near Collection Room",
            "Open Air Meeting Space"
        )
    )
    lectureList.add(
        arrayOf(
            "Vesicle",
            "Oases",
            "Eastern Labs",
            "First Floor",
            "In front of CBI counter",
            "Open Air Meeting Space"
        )
    )
    return lectureList
}

private fun getFloor(s: String): Int {
    return when (s.trim { it <= ' ' }.toLowerCase()) {
        "first floor" -> 1
        "second floor" -> 2
        "ground floor" -> 0
        "third floor" -> 3
        "basement" -> -1
        else -> -2
    }
}

fun getLocations(): List<Location> {
    val returnList = mutableListOf<Location>()
    for (a in listHalls()) {
        returnList.add(Location(a[0], a[1], a[4], a[2], getFloor(a[3]), a[5]))
    }
    return returnList
}