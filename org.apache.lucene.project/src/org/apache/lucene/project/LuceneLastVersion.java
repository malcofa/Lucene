/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.apache.lucene.project;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Mauricio
 */
public class LuceneLastVersion {

    public static void main(String[] args) throws IOException, ParseException {
        /**
         * Se crea un indice y se añaden terminos (esta comentado porque crea indices por cada ejecución)
         */
        /*FSDirectory dir = FSDirectory.open(Paths.get("C:\\mauricio-lucene"));
        IndexWriterConfig config = new IndexWriterConfig();
        IndexWriter w = new IndexWriter(dir, config); 
        addDoc(w, "1", "20-04-2017","Estamos todos Bien en el Refugio"); 
        addDoc(w, "2", "01-05-2017","este es un mensaje sin minisculas"); 
        addDoc(w, "3", "07-12-2016","ESTE ES UN MENSAJE CON MAYUSCULAS"); 
        addDoc(w, "4", "07-01-2017","Este Mensaje _ Contiene ., caracteres ?´+");
        w.close();*/
        /**
         * Se contruye la query
         */
        Analyzer analyzer = new StandardAnalyzer();
        String querystr = args.length > 0 ? args[0] : "es"; 
        Query q = new QueryParser("mensaje", analyzer).parse(querystr);
        /**
         * Se realiza la busqueda de los 10 primeros resultados
         */
        int hitsPerPage = 10; 
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("C:\\mauricio-lucene"))); 
        System.out.println("Capturo: "+reader.toString());
        IndexSearcher searcher = new IndexSearcher(reader); 
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage); 
        searcher.search(q, collector); 
        TopDocs topdocs = collector.topDocs();
        ScoreDoc[] hits = topdocs.scoreDocs;
        /**
         * Muestra los resultados recorriendo el arreglo hits
         */
        System.out.println("Encontro " + hits.length + " resultados."); 
        for(int i=0;i<hits.length;++i) { 
            int docId = hits[i].doc; 
            Document d = searcher.doc(docId); 
            System.out.println("   " + "Id" + "\t" + "Fecha" + "\t\t" + "Mensaje"); 
            System.out.println((i + 1) + ". " + d.get("id") + "\t" + d.get("fecha") + "\t" + d.get("mensaje")); 
        }

    }
    private static void addDoc(IndexWriter w, String id, String fecha, String mensaje) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("id", id, Field.Store.YES));
        doc.add(new StringField("fecha", fecha, Field.Store.YES));
        doc.add(new TextField("mensaje", mensaje, Field.Store.YES));
        w.addDocument(doc);
    }
    private static ScoreDoc [] score (Query q, IndexSearcher searcher, int hitsPerPage) throws IOException{        
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage); 
        searcher.search(q, collector); 
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        return hits;
    }
}
