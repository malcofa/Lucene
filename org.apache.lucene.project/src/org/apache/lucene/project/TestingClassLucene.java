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
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 *
 * @author Mauricio
 */
public class TestingClassLucene {

    public static void main(String[] args) throws IOException, ParseException {
        /**
         * Se crea un indice y se añaden campos(field) con sus respectivos terminos(term) 
         * NOTA 1: Esta comentado porque se crean archivos cada vez que se ejecuta
         * NOTA 2: Cambiar el String del path por una carpta del sistema 
         * Ej: Paths.get("RUTA DE CARPETA");
         */
        /*FSDirectory dir = FSDirectory.open(Paths.get("C:\\mauricio-lucene"));
        IndexWriterConfig config = new IndexWriterConfig();
        IndexWriter w = new IndexWriter(dir, config); 
        addDoc(w, "Lucene in Action", "193398817"); 
        addDoc(w, "Lucene for Dummies", "55320055Z"); 
        addDoc(w, "Managing Gigabytes", "55063554A"); 
        addDoc(w, "The Art of Computer Science", "9900333X");
        w.close();*/
        /**
         * Se contruye la query
         */
        Analyzer analyzer = new StandardAnalyzer();
        /*String querystr;
        if(args.length > 0){
            querystr = args[0];
        }else{
            querystr = "Science";
        }*/
        /**
         * El codigo de arriba, representa lo mismo que el de abajo.
         */
        String querystr = args.length > 0 ? args[0] : "Action"; 
        Query q = new QueryParser("title", analyzer).parse(querystr);
        /**
         * Se realiza la busqueda de los 10 primeros resultados
         */
        int hitsPerPage = 10; 
        IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("C:\\mauricio-lucene"))); 
        System.out.println("Capturo: "+reader.toString());
        IndexSearcher searcher = new IndexSearcher(reader); 
        TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage); 
        searcher.search(q, collector); 
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        /**
         * Muestra los resultados recorriendo el arreglo hits
         */
        System.out.println("Encontro " + hits.length + " resultados."); 
        for(int i=0;i<hits.length;++i) { 
            int docId = hits[i].doc; 
            Document d = searcher.doc(docId); 
            System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title")); 
        }

    }
    private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
        Document doc = new Document();
        doc.add(new TextField("title", title, Field.Store.YES));
        doc.add(new StringField("isbn", isbn, Field.Store.YES));
        w.addDocument(doc);
    }
}
