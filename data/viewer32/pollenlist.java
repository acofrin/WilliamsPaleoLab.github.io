//
// PollenList.java - Read a list of pollen types
// Version - 3.2
// 7/3/2002
// Phil Leduc
// Copyright (c)  Geology Dept., Brown University
//
//

// *******************************************************

class pollenlist {

     private int ListLength;
     private String FilePrefix[][];
     private String PollenName[][];
     private int AlternateLink[][];

     private String PLInput[];

     private final int Common = 0;
     private final int Latin = 1;

     private int DefaultNomenclature;
     private int DefaultSelection; // Picea in Latin array

  public void initializeLists() {

     int i;

     // Edit ListLength, Defaults and the PLInput list below.
     ListLength = 80;  // <== Edit this line.
     DefaultSelection = 44;  // Picea in Latin array - after sorting!
     DefaultNomenclature = Latin;

     PLInput = new String[ListLength];  // Input records: prefix, latin name
                                        // and common name
     FilePrefix = new String[2][ListLength];
     PollenName = new String[2][ListLength];
     AlternateLink = new int[2][ListLength];


     // <== Edit the list below and ListLength above.
     // Pollen List Input - delimited by "~"
     i = 0;
     PLInput[i++]="fi2~Abies (1, 2 and 4%)~Fir (1, 2 and 4%)";
     PLInput[i++]="fir~Abies (1, 5 and 10%)~Fir (1, 5 and 10%)";
     PLInput[i++]="hfd~Abies, Larix/Pseudotsuga and Tsuga~Fir, Larch/Douglas Fir and Hemlock";
     PLInput[i++]="map~Acer~Maple";
     PLInput[i++]="ald~Alnus~Alder";
     PLInput[i++]="gam~Amaranthaceae/Chenopodiaceae~Amaranth/Goosefoot families";
     PLInput[i++]="rag~Ambrosia~Ragweed";
     PLInput[i++]="sag~Artemisia~Sage";
     PLInput[i++]="ast~Asteraceae~Aster family";
     PLInput[i++]="bi2~Betula (5, 10 and 20%)~Birch (5, 10 and 20%)";
     PLInput[i++]="bir~Betula (5, 20 and 40%)~Birch (5, 20 and 40%)";
     PLInput[i++]="ssb~Betula, Picea and Cyperaceae~Birch, Spruce and Sedge";
     PLInput[i++]="sab~Betula, Picea and Fraxinus~Birch, Spruce and Ash";
     PLInput[i++]="bio~Biomes~Biomes";
     PLInput[i++]="btr~Broadleaf Trees and Shrubs~Broadleaf Trees and Shrubs";
     PLInput[i++]="hic~Carya~Hickory";
     PLInput[i++]="hoe~Carya, Quercus and Ulmus~Hickory, Oak and Elm";
     PLInput[i++]="che~Castanea~Chestnut";
     PLInput[i++]="gam~Chenopodiaceae/Amaranthaceae~Goosefoot/Amaranth families";
     PLInput[i++]="cyp~Cupressaceae~Taxaceae~Cypress/Yew families";
     PLInput[i++]="sed~Cyperaceae~Sedge family";
     PLInput[i++]="ssb~Cyperaceae, Betula and Picea~Sedge, Birch and Spruce";
     PLInput[i++]="ssp~Cyperaceae, Pinus and Picea~Sedge, Pine and Spruce";
     PLInput[i++]="ele~Elevation~Elevation";
     PLInput[i++]="he2~Ericaceae (1, 2 and 4%)~Heath family (1, 2 and 4%)";
     PLInput[i++]="hea~Ericaceae (1, 5 and 10%)~Heath family (1, 5 and 10%)";
     PLInput[i++]="bee~Fagus~Beech";
     PLInput[i++]="hbp~Fagus, Pinus and Tsuga~Beech, Pine and Hemlock";
     PLInput[i++]="ash~Fraxinus~Ash";
     PLInput[i++]="sab~Fraxinus, Betula and Picea~Ash, Birch and Spruce";
     PLInput[i++]="hrb~Herbs~Herbs";
     PLInput[i++]="wal~Juglans~Walnut";
     PLInput[i++]="ld2~Larix/Pseudotsuga (1, 2 and 4%)~Larch/Douglas Fir (1, 2 and 4%)";
     PLInput[i++]="ldf~Larix/Pseudotsuga (1, 5 and 10%)~Larch/Douglas Fir (1, 5 and 10%)";
     PLInput[i++]="hfd~Larix/Pseudotsuga, Tsuga and Abies~Larch/Douglas Fir and Hemlock, Fir";
     PLInput[i++]="sgu~Liquidambar~Sweetgum";
     PLInput[i++]="bay~Myrica~Sweet gale";
     PLInput[i++]="ntr~Needleleaf Trees and Shrubs~Needleleaf Trees and Shrubs";
     PLInput[i++]="so2~Nyssa (1, 2 and 4%)~Tupelo (1, 2 and 4%)";
     PLInput[i++]="sog~Nyssa (1, 5 and 10%)~Tupelo (1, 5 and 10%)";
     PLInput[i++]="hho~Ostrya Carpinus~Hornbeam";
     PLInput[i++]="ms2~Oxyria digyna (1, 2 and 4%)~Mountain Sorrel (1, 2 and 4%)";
     PLInput[i++]="mso~Oxyria digyna (1, 5 and 10%)~Mountain Sorrel (1, 5 and 10%)";
     PLInput[i++]="geo~Paleogeography with Ice Sheets~Paleogeography with Ice Sheets";
     PLInput[i++]="spd~Picea~Spruce";
     PLInput[i++]="ssb~Picea, Cyperaceae and Betula~Spruce, Sedge and Birch";
     PLInput[i++]="ssp~Picea, Cyperaceae and Pinus~Spruce, Sedge and Pine";
     PLInput[i++]="sab~Picea, Fraxinus and Betula~Spruce, Ash and Birch";
     PLInput[i++]="fos~Picea, Praire Forbs and Quercus~Spruce, Prairie Forbs and Oak";
     PLInput[i++]="pid~Pinus~Pine";
     PLInput[i++]="piw~Pinus strobus~White Pine";
     PLInput[i++]="ssp~Pinus, Picea and Cyperaceae~Pine, Spruce and Sedge";
     PLInput[i++]="hbp~Pinus, Tsuga and Fagus~Pine, Hemlock and Beech";
     PLInput[i++]="syc~Platanus~Sycamore";
     PLInput[i++]="gra~Poaceae~Grass family";
     PLInput[i++]="pop~Populus~Poplar";
     PLInput[i++]="fos~Praire Forbs, Quercus and Picea~Prairie Forbs, Oak and Spruce";
     PLInput[i++]="pfo~Prairie Forbs (No Ambrosia; 5, 10 and 20%)~Prairie Forbs (No Ragweed; 5, 10 and 20%)";
     PLInput[i++]="pf2~Prairie Forbs (No Ambrosia; 5, 20 and 40%)~Prairie Forbs (No Ragweed; 5, 20 and 40%)";
     PLInput[i++]="pfa~Prairie Forbs (With Ambrosia; 5, 10 and 20%)~Prairie Forbs (With Ragweed; 5, 10 and 20%)";
     PLInput[i++]="pa2~Prairie Forbs (With Ambrosia; 5, 20 and 40%)~Prairie Forbs (With Ragweed; 5, 20 and 40%)";
     PLInput[i++]="ld2~Pseudotsuga/Larix (1, 2 and 4%)~Douglas Fir/Larch (1, 2 and 4%)";
     PLInput[i++]="ldf~Pseudotsuga/Larix (1, 5 and 10%)~Douglas Fir/Larch (1, 5 and 10%)";
     PLInput[i++]="hfd~Pseudotsuga/Larix, Tsuga and Abies~Douglas Fir/Larch and Hemlock, Fir";
     PLInput[i++]="oa2~Quercus (5, 10 and 20%)~Oak (5, 10 and 20%)";
     PLInput[i++]="oak~Quercus (5, 20 and 40%)~Oak (5, 20 and 40%)";
     PLInput[i++]="fos~Quercus, Picea and Praire Forbs~Oak, Spruce and Prairie Forbs";
     PLInput[i++]="hoe~Quercus, Ulmus and Carya~Oak, Elm and Hickory";
     PLInput[i++]="wi2~Salix (1, 2 and 4%)~Willow (1, 2 and 4%)";
     PLInput[i++]="wil~Salix (1, 5 and 10%)~Willow (1, 5 and 10%)";
     PLInput[i++]="sit~Site Distribution~Site Distribution";
     PLInput[i++]="cyp~Taxaceae/Cupressaceae~Yew/Cypress families";
     PLInput[i++]="bcy~Taxodium~Bald Cypress";
     PLInput[i++]="avi~Thuja~Arbor Vitae";
     PLInput[i++]="bas~Tilia~Basswood";
     PLInput[i++]="hem~Tsuga~Hemlock";
     PLInput[i++]="hfd~Tsuga, Abies and Larix/Pseudotsuga~Hemlock, Fir and Larch/Douglas Fir";
     PLInput[i++]="hbp~Tsuga, Fagus and Pinus~Hemlock, Beech and Pine";
     PLInput[i++]="elm~Ulmus~Elm";
     PLInput[i++]="hoe~Ulmus, Carya and Quercus~Elm, Hickory and Oak";

     // Parse out the first field of each record for the latin
     // pollen list.
     parseColumn(1,PLInput,FilePrefix[Latin]);

     // Parse out the second field which contains the latin
     // pollen names.
     parseColumn(2,PLInput,PollenName[Latin]);

     // Parse out the third field which contains the common
     // pollen names.
     parseColumn(3,PLInput,PollenName[Common]);

     // Sort the FilePrefix and two PollenName arrays
     // using the values of the latin pollen names.
     sortInputColumns(Latin);

     // Duplicate the prefixes for the common pollen list.
     // Set the common alternate links which point to latin names
     for (i=0; i<ListLength; i++) {
        FilePrefix[Common][i] = FilePrefix[Latin][i];
        AlternateLink[Common][i] = i;
     }

     // Sort the three arrays associated with Common data:
     // FilePrefix, PollenName and AlternateLinks
     sortListTypeColumns(Common);

     for (i=0; i<ListLength; i++) {
        AlternateLink[Latin][AlternateLink[Common][i]] = i;
     }

     return;

  }

  public void sortInputColumns(int ListType)  {

     // Uses Selection sort algorithm
     // Rejected bubblesort since it's slowest algorithm
     // Rejected quicksort since it requires recursion.
     // See SOFTWARE ENGINEERING FOR SMALL COMPUTER,
     // R.B.Coats, 1982, page 123.

     String tempStr;
     int min,i,j,tempInt;
     boolean swap;

     // Reorder all pollen info based on the selected list type.
     for (i=0; i < ListLength-1; i++) {

        min = i;
        swap = false;
        for (j=i+1; j < ListLength; j++) {

           // if the jth item value < the minimum value
           if (PollenName[ListType][j].compareTo(PollenName[ListType][min])< 0){
              min = j;
              swap = true;
           }

        }

        // Swap pollen name, file prefix and original index enties.
        if (swap) {
          tempStr = PollenName[Latin][i];
          PollenName[Latin][i] = PollenName[Latin][min];
          PollenName[Latin][min] = tempStr;

          tempStr = FilePrefix[Latin][i];
          FilePrefix[Latin][i] = FilePrefix[Latin][min];
          FilePrefix[Latin][min] = tempStr;

          tempStr = PollenName[Common][i];
          PollenName[Common][i] = PollenName[Common][min];
          PollenName[Common][min] = tempStr;
        }
     }

     return;

  }

  public void sortListTypeColumns(int ListType)  {

     // Uses Selection sort algorithm
     // Rejected bubblesort since it's slowest algorithm
     // Rejected quicksort since it requires recursion.
     // See SOFTWARE ENGINEERING FOR SMALL COMPUTER,
     // R.B.Coats, 1982, page 123.

     String tempStr;
     int min,i,j,tempInt;
     boolean swap;

     // Reorder all pollen info based on the selected list type.
     for (i=0; i < ListLength-1; i++) {

        min = i;
        swap = false;
        for (j=i+1; j < ListLength; j++) {

           // if the jth item value < the minimum value
           if (PollenName[ListType][j].compareTo(PollenName[ListType][min])< 0){
              min = j;
              swap = true;
           }

        }

        // Swap pollen name, file prefix and original index enties.
        if (swap) {
          tempStr = PollenName[ListType][i];
          PollenName[ListType][i] = PollenName[ListType][min];
          PollenName[ListType][min] = tempStr;

          tempStr = FilePrefix[ListType][i];
          FilePrefix[ListType][i] = FilePrefix[ListType][min];
          FilePrefix[ListType][min] = tempStr;

          tempInt = AlternateLink[ListType][i];
          AlternateLink[ListType][i] = AlternateLink[ListType][min];
          AlternateLink[ListType][min] = tempInt;
        }
     }

     return;

  }

  public void parseColumn(int ColumnInt,
                          String[] TildeString, String[] Column) {

     // Stores the selected column in the Column array.
     // Note: Column numbering starts with 1.
     for (int i=0; i<ListLength; i++) {
        // Parse a single string.
        Column[i] = parseTildeSubstring(ColumnInt,TildeString[i]);
     }

     return;
  }

  public String parseTildeSubstring(int SubstringInt,
                                    String TildeString) {

     // Retrieves a substring of a Tilde delimited string
     // Note: SubstringInt/column numbering starts with 1.
     //       Array indexing starts with 0.

     // Initialize tilde indexes for use in the loop below.
     int LastTildeIndex = -2;
     int NextTildeIndex = -1;
     String Substring,WorkString;

     // Pad the input string in order to guarentee two tildes and
     // thus simplify the loop's conditional logic.
     WorkString =  "~" + TildeString + "~";

     // Find the string/array indexes of the tildes that enclose
     //the selected substring field.
     for (int i=0; i<SubstringInt + 1; i++) {

        if (NextTildeIndex > LastTildeIndex) {
           LastTildeIndex = NextTildeIndex;
        }
        // indexOf returns a -1 if tilde is not found else
        // it returns number >= LastTildeIndex + 1
        NextTildeIndex = WorkString.indexOf("~", LastTildeIndex + 1);

     }

     // If there is at least one character between the two tildes,
     // return the string between the tildes, else return a null string.
     if (NextTildeIndex - LastTildeIndex > 2) {
        Substring = WorkString.substring(LastTildeIndex + 1,
                                          NextTildeIndex);
     }
     else
        Substring = "";

     return Substring;
  }

  public int getListLength() {

     return ListLength;

  }

  public int getCommonConstant() {

     return Common;

  }

  public int getLatinConstant() {

     return Latin;

  }

  public boolean getPollenNames(int ListType, String[] Name) {

     if ((ListType == Common) || (ListType == Latin)) {

        for (int i=0; i<ListLength; i++)
           Name[i] = PollenName[ListType][i];

        return true;
     }
     else
        return false;
  }

  public int getAlternateLink(int ListType, String Pollen) {

     int i;
     int AltLink = 0;
     boolean Found = false;

     if ((ListType == Common) || (ListType == Latin)) {

        for (i=1; ((i<ListLength) && (!Found)); i++) {

           if (Pollen == PollenName[ListType][i]) {
              AltLink = AlternateLink[ListType][i];
              Found = true;
           }

        }

     }

     return AltLink;
  }

  public int getAlternateLink(int ListType, int OriginalIndex) {

     int AltLink = 0;

     if (((ListType == Latin) || (ListType == Common)) &&
          ((OriginalIndex >= 0) && (OriginalIndex < ListLength))) {

        AltLink = AlternateLink[ListType][OriginalIndex];
     }

     return AltLink;
  }

  public String getFilePrefix(int ListType, int SelectionIndex) {


     return FilePrefix[ListType][SelectionIndex];
  }

  public int getDefaultNomenclature() {

     return DefaultNomenclature;
  }

  public int getDefaultSelectionIndex() {

     return DefaultSelection;
  }

}

