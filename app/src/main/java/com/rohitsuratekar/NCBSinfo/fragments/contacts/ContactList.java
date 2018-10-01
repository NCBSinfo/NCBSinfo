package com.rohitsuratekar.NCBSinfo.fragments.contacts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Rohit Suratekar on 14-10-17 for NCBSinfo.
 * All code is released under MIT License.
 */

class ContactList {

    List<ContactModel> getAll() {
        List<ContactModel> models = new ArrayList<>();

        for (Object o : all_list) {
            Object[] item = (Object[]) o;
            ContactModel model = new ContactModel();
            model.setType((String) item[0]);
            model.setName((String) item[1]);
            model.setPrimaryExtension((String) item[3]);
            model.setLocation((String) item[2]);
            List<String> other = new ArrayList<>(Arrays.asList((String[]) item[4]));
            model.setOtherExtensions(other);
            model.setInstitute((String) item[5]);
            model.setDetails((String) item[6]);

            models.add(model);
        }

        return models;

    }

    private Object[][] all_list = {
            {"Office", "Director", "Directors Office ", "6260", new String[]{"6302"}, "NCBS", "Satyajit Mayor"},
            {"Office", "Directors Office", "Directors Office ", "6301", new String[]{}, "NCBS", "Sheela"},
            {"Office", "Dean", "Deans Office", "6130", new String[]{}, "NCBS", "Upinder S Bhalla"},
            {"Office", "Dean", "Deans Office", "6401", new String[]{}, "InStem", "Apurva Sarin"},
            {"Office", "Dean", "Deans Office", "6820", new String[]{}, "InStem", "S.Ramaswamy"},
            {"Office", "Deans Office", "Deans Office", "6403", new String[]{"6323"}, "NCBS", "Shantha, Sujatha N "},
            {"Office", "Deans Office", "Deans Office", "6206", new String[]{}, "InStem", "Valsala. N "},
            {"Office", "Academic Office", "Academic Office ", "6404", new String[]{}, "NCBS", "KS Vishalakshi "},
            {"Office", "Admin Office", "Admin Office ", "6335", new String[]{}, "NCBS", "NULL"},
            {"Office", "Admin Office", "Admin Office ", "6205", new String[]{}, "InStem", "NULL"},
            {"Office", "Admin Office", "Admin Office ", "5008", new String[]{"5052"}, "CCAMP", "NULL"},
            {"Office", "Finance", "ELC", "6332", new String[]{}, "NCBS", "Ashok Rao, Pragati"},
            {"Office", "Finance", "ELC  ", "6114", new String[]{}, "InStem", "Basavaraj.K.M "},
            {"Service", "Stores", "Stores ", "6423", new String[]{"6424"}, "ALL", "Prashanth K "},
            {"Service", "Despatch", "ELC GF", "6336", new String[]{}, "ALL", "Pratap, Girish "},
            {"Office", "RDO", "ELC TF", "6224", new String[]{}, "NCBS", "Malini S Pillai"},
            {"Office", "Science & Society", "NULL", "6516", new String[]{}, "NCBS", "Venkat Srinivasan "},
            {"Service", "India Bio Science", "ELC", "6223", new String[]{}, "NONE", "Smita Jain "},
            {"Service ", "Meetings Office", "ELC FF", "6337", new String[]{"6337"}, "NCBS", "Bhavya S"},
            {"Service ", "Travel Desk", "ELC FF", "6365", new String[]{"6365"}, "NCBS", "Sharath M R "},
            {"Facility ", "CIFF", "ELC BS", "6013", new String[]{"6014"}, "NCBS", "Krishnamurthy HS "},
            {"Facility ", "CIFF", "SLC BS", "6939", new String[]{"6940"}, "NCBS", "Manoj Mathew "},
            {"Service", "Lab Support", "ELC GF", "6065", new String[]{"6064"}, "NCBS", "Ranjith PP, Manjunath N "},
            {"Service", "IT Section", "SLC BS", "6920", new String[]{"6421"}, "NCBS", "Chakrapani, Prasanta Kumar Baruah, Rajesh R "},
            {"Service", "Instrumentation", "ELC", "6066", new String[]{"6052"}, "NCBS", "Gautam P C , Jayaprakash "},
            {"Facility ", "Masspsec", "ELC BS", "6498", new String[]{}, "NCBS", "NULL"},
            {"Facility ", "Animal House", "NULL", "6432", new String[]{"6433", "6434", "6435"}, "NCBS", "Mohan GH "},
            {"Facility ", "Bio-safety Lab", "ELC", "6047", new String[]{}, "NCBS", "NULL"},
            {"Facility ", "Nomic Facility", "NULL", "6277", new String[]{}, "NCBS", "NULL"},
            {"Facility ", "Screening Facility", "ELC FF", "6356", new String[]{}, "NCBS", "NULL"},
            {"Facility ", "Sequencing Facility", "SLC GF", "6068", new String[]{}, "NCBS", "NULL"},
            {"Facility ", "Electron Microscopy Facility", "ELC", "6496", new String[]{}, "NCBS", "NULL"},
            {"Room", "Equipment Room - 16", "NULL", "6204", new String[]{}, "NCBS", "NULL"},
            {"Service", "Electronic Workshop", "ELC GF", "6046", new String[]{}, "NCBS", "M.N.Karthikeyan, Dora Babu"},
            {"Service", "Mechanical Workshop", "NULL", "6422", new String[]{}, "NCBS", "Deva kumar "},
            {"Service", "Library", "SLC SF", "6026", new String[]{"6025"}, "ALL", "Avinash D Chinchure, Umasashi.S "},
            {"Office", "Infrastructure & Construction", "NULL", "6350", new String[]{}, "NCBS", "V. Rengaswamy "},
            {"Office", "Civil Engineering", "NULL", "6357", new String[]{}, "NCBS", "B.V.Chowdappa "},
            {"Office", "Electrical", "NULL", "6387", new String[]{"6358"}, "NCBS", "Anand Kumar.V , Murugan P "},
            {"Service", "Air Conditioning", "NULL", "6427", new String[]{"6429"}, "NCBS", "H.S.Venkataramana, Vipin. V "},
            {"Office", "Architect", "ELC", "6360", new String[]{"6355"}, "NCBS", "Poornima U B, Savitha.K.S "},
            {"Service", "Civil Maintenance", "NULL", "6353", new String[]{"6357"}, "NCBS", "Basavaraja HM, Natarajan T "},
            {"IMP", "Medical Facility", "Medical Centre", "6450", new String[]{"6449"}, "ALL", "Dr Vishwanath N Patil, Dr Krupa G, Dr.Tehzib Saiyed  "},
            {"Service", "Main Canteen", "Canteen", "6436", new String[]{"6438"}, "ALL", "Prashanth Murthy "},
            {"Office", "Hospitality", "NULL", "6437", new String[]{}, "ALL", "Shaju Varghese "},
            {"Hostel ", "Champaka", "Champaka ", "6451", new String[]{}, "NCBS", "NULL"},
            {"Hostel ", "Mandara", "Mandara ", "28561721", new String[]{}, "NCBS", "NULL"},
            {"Hostel ", "Canara Bank Layout", "Canara Bank Layout ", "32213134", new String[]{}, "NCBS", "NULL"},
            {"IMP", "Security Office", "NULL", "6003", new String[]{"6004", "6005", "6022"}, "ALL", "Krishna Pillai "},
            {"Service", "Sports Facility", "NULL", "6099", new String[]{}, "ALL", "Baba , Kumar"},
            {"IMP", "Reception", "ELC GF", "6001", new String[]{"6002", "6018", "6019", "6562", "6304"}, "NCBS", "NULL"},
            {"Service", "Academic Block Cafeteria", "ELC FF", "6440", new String[]{}, "ALL", "NULL"},
            {"Service", "Admin Block Cafeteria", "ELC SF", "6441", new String[]{}, "ALL", "NULL"},
            {"Service", "Bank Counter", "SLC FF", "6188", new String[]{}, "ALL", "Central Bank of India "},
            {"IMP", "Sub Station", "NULL", "6425", new String[]{"6426"}, "ALL", "NULL"},
            {"Service", "Swimming Pool", "NULL", "6009", new String[]{}, "ALL", "NULL"},
            {"Service", "Guest House", "NULL", "6451", new String[]{}, "ALL", "NULL"},
            {"Room", "Dasheri", "SLC GF", "6569", new String[]{}, "NCBS", "200 seater"},
            {"Room", "Malgova", "SLC SF", "6762", new String[]{}, "NCBS", "70 seater"},
            {"Room", "Nucleus", "ELC GF", "6055", new String[]{}, "NCBS", "NULL"},
            {"Room", "Chloroplast", "ELC SF", "6255", new String[]{}, "NCBS", "NULL"},
            {"Room", "Golgi", "ELC FF", "6155", new String[]{}, "NCBS", "NULL"},
            {"Room", "Banganapalli", "ELC BS", "6407", new String[]{}, "NCBS", "Old Teaching Lab"},
            {"Room", "Mitochondria", "SLC SF", "6755", new String[]{}, "NCBS", "NULL"},
            {"Room", "Synapse", "SLC GF", "6555", new String[]{}, "NCBS", "NULL"},
            {"Room", "Axon", "ELC SF", "6090", new String[]{}, "NCBS", "Video Conferencing room"},
            {"Room", "Bleb", "ELC GF", "6113", new String[]{}, "NCBS", "Admin Block Ground Floor"},
            {"Room", "Cilium", "ELC FF", "6315", new String[]{}, "NCBS", "Admin Block First Floor"},
            {"Room", "Admin Meeting Room", "NULL", "6321", new String[]{}, "NCBS", "NULL"},
            {"Room", "Ribosome", "CCAMP", "5131", new String[]{}, "CCAMP", "C-Camp Ground Floor "},
            {"Room", "Centriole", "CCAMP", "5231", new String[]{}, "CCAMP", "C-Camp First Floor"},
            {"Service", "Dolna", "NULL", "6453", new String[]{"6153", "6452"}, "ALL", "Creche"},
            {"Service", "Travel Agent", "NULL", "6336", new String[]{}, "NCBS", "Jaybee Travels"},
            {"IMP", "Reception", "CCAMP", "5008", new String[]{}, "CCAMP", "NULL"},
            {"Facility ", "Fly Facility", "ELC SF", "6232", new String[]{"6231"}, "NCBS", "Deepti Trivedi Vyas "},
            {"Service", "C-Camp Kitchen", "NULL", "5214", new String[]{}, "CCAMP", " Nanjappa "},
            {"Service", "SLC Reception", "SLC GF", "6569", new String[]{}, "NCBS", "Key Issue"},
            {"Room", "SLC Ground Floor", "SLC GF", "6506", new String[]{"6507", "6504", "6509", "6510", "6520", "6515", "6514"}, "NCBS", "Student Area"},
            {"Room", "SLC First Floor", "SLC FF", "6601", new String[]{"6602", "6603", "6604", "6605", "6607", "6608", "6609"}, "NCBS", "Student Area"},
            {"Room", "SLC First Floor", "SLC FF", "6602", new String[]{}, "NCBS", "Rohit Suratekar"},
            {"Room", "SLC Second Floor", "SLC SF", "6701", new String[]{"6702", "6703", "6704", "6705", "6706", "6707", "6708"}, "NCBS", "Student Area"},
            {"Room", "SLC Third Floor", "SLC TF", "6803", new String[]{"6804", "6806", "6808"}, "NCBS", "Student Area"},
            {"Service", "SLC Lab Kitchen", "SLC FF", "6670", new String[]{}, "NCBS", "NULL"},
            {"Service", "SLC Lab Kitchen", "SLC SF", "6770", new String[]{}, "NCBS", "NULL"},
            {"Service", "Server Room", "NULL", "6948", new String[]{}, "NCBS", "NULL"},
            {"Service", "Telephone Exchange", "NULL", "6000", new String[]{"6500"}, "NCBS", "NULL"},
            {"Service", "Pump House", "NULL", "6485", new String[]{}, "NCBS", "NULL"}
    };


}
