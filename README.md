# Lucene with CISI dataset
Information Retrieval with Lucene and CISI dataset

 This is an example of how you can use Lucene to Information Retrieval with the CISI dataset. Index documents and search between them with IB, DFR, BM-25, TF-IDF, Boolean, Axiomatic, LM-Dirichlet similarity. You can enable and disable stemmer and set custom stop words. We use Lucene version 9.5.0 in this project. Don't forget to change the paths inside the code to your computer, then run it. You can use Eclipse to open and run this project.


 <img src="/screenshot.webp" />

 
 ### Query
  You can write query easily like ```Lending book``` or for advanced search you can use this format
  ```docTitle="" docContent="" docAuthors=""``` to find best results.

 ### Evaluation
  There are 111 queries with the most relevant results in order of relevance in the CISI dataset. In the evaluation section, we check how similar our results are to the best results. So we calculate Recall, Precision, MAP (Mean Average Precision) and F-Measure for all queries.


 ### Resources
 1. <a href="https://github.com/apache/lucene" alt="Apache Lucene">Lucene</a>
 2. <a href="https://www.kaggle.com/datasets/dmaso01dsta/cisi-a-dataset-for-information-retrieval" alt="CISI dataset">CISI dataset</a>

 
 ### Developed by
 1. Behrouz Amoushahi
 2. Mehdi Jabalameli
