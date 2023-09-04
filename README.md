# simple-excel
Simple excel reader, to read out batches of rows or columns.
I needed a simpler version of Apache POI for my own project, but you are welcome to use it or PR me some improvements.

**DISCLAIMER:** 
The code isn't built for speed, but for simplicity. Especially the column reading should be quite slow. 
It is probably better to read them as rows and then transpose them, but as of right now I don't have the time.
