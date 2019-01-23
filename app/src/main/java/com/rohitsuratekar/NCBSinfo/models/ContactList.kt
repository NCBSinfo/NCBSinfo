package com.rohitsuratekar.NCBSinfo.models


fun getAllContacts(): List<Contact> {
    val returnList = mutableListOf<Contact>()
    for (a in allNumbers) {
        val contact = Contact().apply {
            type = a[0]
            name = a[1]
            primaryExtension = a[2]
        }
        if (a[4] != "None") {
            contact.details = a[4]
        }
        if (a[3] != "None") {
            contact.otherExtensions = a[3]
        }
        returnList.add(contact)
    }
    return returnList
}

private val allNumbers = arrayListOf(
    arrayOf("emergency", "Emergency", "6666", "None", "None"),
    arrayOf("emergency", "Reception", "6001", "6002, 6018, 6019", "None"),
    arrayOf(
        "emergency",
        "Medical Facility",
        "6450",
        "6449",
        "Vishwanath N Patil, Krupa G, Divyakuttikrishnan, Tehzib Saiyed, doctor"
    ),
    arrayOf("emergency", "Sub Station", "6425", "6426", "None"),
    arrayOf("emergency", "Security", "6022", "6003, 6004, 6005", "Krishna Pillai"),
    arrayOf("emergency", "Reception CCAMP", "5008", "None", "None"),
    arrayOf("person", "Director NCBS", "6260", "6302", "Satyajit Mayor"),
    arrayOf("person", "Director InStem", "6401", "None", "Apurva Sarin"),
    arrayOf("office", "Directors Office", "6301", "None", "Sheela"),
    arrayOf("person", "Dean", "6130", "None", "Upinder Bhalla"),
    arrayOf("person", "Dean", "6820", "None", "S Ramaswamy"),
    arrayOf("person", "Head Academic", "6401", "None", "Mukund thattai"),
    arrayOf("office", "Deans Office", "6403", "6323, 6206", "Shantha, Sujatha N, Valsala"),
    arrayOf("office", "Academic Office", "6404", "None", "KS Vishalakshi"),
    arrayOf("office", "NCBS Admin Office", "6335", "None", "None"),
    arrayOf("office", "inStem Admin Office", "6205", "None", "None"),
    arrayOf("office", "C-CAMP Admin Office", "5008", "5052", "None"),
    arrayOf("office", "Projects Office", "6357", "None", "Basavaraja HM"),
    arrayOf(
        "department",
        "Administration Accounts",
        "6334",
        "6328, 6332, 6114, 6333, 6205",
        "Ashok Rao, Basavaraj, Krishnamaraju, Maithly, Ramprasad, Pandian, Srinidhi, Thanuja, Uma, Valsala Neyyan"
    ),
    arrayOf(
        "department",
        "Purchase",
        "6344",
        "6345, 6343, 6342, 6327",
        "Chetana, Lakshmi Priya, Nirmala, Ramanathan, Sreenath"
    ),
    arrayOf("department", "Stores", "6423", "6423", "Prashanth "),
    arrayOf("department", "Despatch", "6336", "None", "Pratap, Girish"),
    arrayOf(
        "department",
        "Research & Development",
        "6227",
        "6227, 6228, 6403",
        "RDO, Vineetha, Pankaj, Dhruba, Malini, Athulya "
    ),
    arrayOf("department", "Science & Society", "6516", "6207", "Venkat Srinivasan, Krishnapriya Tamma"),
    arrayOf("department", "Meetings Office", "6337", "None", "Bhavya S"),
    arrayOf("department", "Travel Desk", "6365", "None", "Sharath M R"),
    arrayOf("department", "IT Section", "6420", "6421, 6920", "computer, Chakrapani, Prasanta, Rajesh, Alok B"),
    arrayOf("department", "Instrumentation", "6052", "6066", "Gautam P C, Jayaprakash"),
    arrayOf("department", "Civil Engineering", "6357", "None", "B V Chowdappa"),
    arrayOf(
        "department",
        "Electrical",
        "6425",
        "6358, 6380, 6428, 6430",
        "Anand, Murugan, Ravindra Munshi, Sidhartha Swain, Suresh Kumar"
    ),
    arrayOf("department", "Air Conditioning", "6427", "6429", "ac, H.S.Venkataramana, Basavaraj Jalihal, Vipin V"),
    arrayOf("department", "Architect", "6360", "6355", "Poornima U B, Savitha.K.S"),
    arrayOf("department", "Civil Maintenance", "6353", "6353", "Natarajan T, Rakshith Komalan"),
    arrayOf(
        "department",
        "Hospitality",
        "6438",
        "6437",
        "Prashanth Murthy, Shaju Varghese, Canteen, Guest House, Housekeeping"
    ),
    arrayOf("facility", "CIFF (ELC)", "6014", "6013", "Krishnamurthy HS"),
    arrayOf("facility", "CIFF (SLC)", "6939", "6940, 6277", "None"),
    arrayOf("facility", "Lab Support & Lab Safety ", "6065", "6064", "Ranjith PP, Manjunath N"),
    arrayOf("facility", "Masspsec Lab", "6498", "None", "None"),
    arrayOf("facility", "Animal House", "6434", "6432, 6435", "Mohan GH"),
    arrayOf("facility", "Bio-safety Lab", "6047", "None", "None"),
    arrayOf("facility", "Nomic Facility", "6277", "None", "None"),
    arrayOf("facility", "Screening Facility", "6356", "6068", "None"),
    arrayOf("facility", "EM Facility", "6496", "None", "None"),
    arrayOf("facility", "Electronic Workshop", "6046", "None", "M.N.Karthikeyan, Dora Babu"),
    arrayOf("facility", "Mechanical Workshop", "6422", "None", "Deva kumar"),
    arrayOf("facility", "Library", "6025", "6026", "Avinash D Chinchure, Umasashi S"),
    arrayOf("facility", "Carpentry Shed", "6431", "None", "None"),
    arrayOf("facility", "Guest House (Champaka)", "6451", "None", "None"),
    arrayOf("facility", "Guest House (Mandara)", "28561721", "None", "None"),
    arrayOf("facility", "Guest House (CBL)", "32213134", "None", "None"),
    arrayOf("facility", "Sports Facility", "6099", "None", "Baba, Kumar"),
    arrayOf("facility", "Main Canteen", "6436", "None", "None"),
    arrayOf("facility", "Academic Block Cafeteria", "6440", "None", "None"),
    arrayOf("facility", "Admin Block Cafeteria", "6441", "None", "None"),
    arrayOf("facility", "Bank Counter", "6188", "None", "Central Bank of India, CBI"),
    arrayOf("facility", "Dolna ", "6453", "6153, 6452", "None"),
    arrayOf("facility", "Fly Facility", "6232", "6231", "Deepti Trivedi Vyas"),
    arrayOf("facility", "SLC Key Issue", "6569", "None", "None"),
    arrayOf("facility", "Sever Room", "6948", "None", "None"),
    arrayOf("facility", "Telephone Exchange", "6000", "6500", "None"),
    arrayOf("facility", "Pump House", "6485", "None", "None")
)



