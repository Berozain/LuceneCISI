# Information Retrieval with Lucene and CISI dataset
 Index documents and search between them with IB, DFR, BM-25, TF-IDF, Boolean, Axiomatic, LM-Dirichlet similarity. You can enable and disable stemmer and set custom stop words. We use Lucene version 9.5.0 in this project. Don't forget to change the paths inside the code to your computer, then run it. You can use Eclipse to open and run this project.

 <img src="/screenshot.webp" />
 ### Query
  You can write query easyly like "Lending book" or for advenced search you can use this format: docTitle="" docContent="" docAuthors=""

 ### Evaluation
  There are 111 queries with the best answers in the CISI dataset. In the evaluation section, we check how similar our results are to the best answers. So we calculate Recall, Precision, MAP (Mean Average Precision) and F-Measure for all queries.

 ### Resources
 1. <a href="https://github.com/apache/lucene" alt="Apache Lucene">Lucene</a>
 2. <a href="https://www.kaggle.com/datasets/dmaso01dsta/cisi-a-dataset-for-information-retrieval" alt="CISI dataset">CISI dataset</a>

 
 ### Developed by
 1. Behrouz Amoushahi Khouzani
 2. Mehdi Jabalameli
