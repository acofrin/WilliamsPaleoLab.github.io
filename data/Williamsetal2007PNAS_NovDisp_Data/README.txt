README
Jack Williams
These files contain the climate-dissimilarity results reported in Williams et al. 2007 PNAS 

The output data is provided in two formats: ASCII text and NetCDF.  (A few files have also been converted to Microsoft Excel.

The ASCII text files are stored in tabular form, with the following field order:
1) ID for target gridcell
2) ID for gridcell of closest analogue
3) Latitude for target gridcell
4) Longitude for target gridcell
5) Laitutde for closest-analogue gridcell
6) Longitude for closest-analogue gridcell
7) Standardized Euclidean distance between target gridcell and its closest analogue
8) An empty column, not used in this analysis

The NetCDF files store the same information, but as rasters.  

The file name convention is:  [Run Number]_[GCM Name]_[Scenario]_gl.txt
For ensembles (i.e. where the results from individual models are averaged together), the file names are:
[Run Number]_[Scenario]_gl.txt


The following runs are stored here:
133:  Dissimilarities between each 21st-century target gridcell and its closest 20th-century analogue (“novel” climates).  These results are shown in Panels 2C and 2D of Williams et al. 2007.
134:  Dissimilarities calculated between the late 20th-century and late 21st-century climate for each gridcell (“local” climate change).  These results are shown in Panels 2A and 2B of Williams et al. 2007.
135:  Dissimilarities between each 20th-century target gridcell and its closest 21st-century analogue (“disappearing” climates).  These results are shown in Panels 2E and 2F of Williams et al. 2007.
136:  As run 133, except that a 500-km constraint was placed on the search for analogues.  High SED values indicate novel climates within this search radius.  These results are shown in Panels 3A and 3B of Williams et al. 2007.
137:  As run 135, except that a 500-km constraint was placed on the search for analogues.  High SED values indicate disappearing climates within this search radius.  These results are shown in Panels 3C and 3D of Williams et al. 2007.

