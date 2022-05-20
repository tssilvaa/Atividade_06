package br.unicamp.ic.inf335.atividade_06.database;

import java.util.Arrays;
import java.util.List;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;
import com.mongodb.client.model.Filters;

public class MongoDB {
	private static MongoClient client = null;
	
	public static MongoCollection<Document> createCollection(String collectionName, MongoDatabase db) {
		
		MongoIterable<String> listaCollections = db.listCollectionNames();
		for (String lista: listaCollections) {
			if (lista.equals(collectionName)) {
				MongoCollection<Document> oldCollection = db.getCollection(collectionName);
				oldCollection.drop();
			}
		}
		
		try {
			db.createCollection(collectionName);
		} catch (Exception e) {
			System.out.println("Erro dao criar coleção :" + e);
			e.printStackTrace();
		}
		
		MongoCollection<Document> colection = db.getCollection(collectionName);
		
		Document produto1 = new Document();
		produto1.append("nome", "Calular Galaxy S10");
		produto1.append("descricao", "128 Gb, Preto, com carregador");
		produto1.append("valor", "1250.0");
		produto1.append("estado", "Poucos riscos, estado de novo");
		
		Document produto2 = new Document();
		produto2.append("nome", "Prod2");
		produto2.append("descricao", "Bla bla");
		produto2.append("valor", "1100.0");
		produto2.append("estado", "Bla bla");
		
		Document produto3 = new Document();
		produto3.append("nome", "Prod3");
		produto3.append("descricao", "Bla bla");
		produto3.append("valor", "120.0");
		produto3.append("estado", "Bla bla");
		
		Document produto4 = new Document();
		produto4.append("nome", "Prod4");
		produto4.append("descricao", "Bla bla");
		produto4.append("valor", "1300.0");
		produto4.append("estado", "Bla bla");
		
		Document produto5 = new Document();
		produto5.append("nome", "Prod5");
		produto5.append("descricao", "Bla bla");
		produto5.append("valor", "9400.0");
		produto5.append("estado", "Bla bla");
		
		Document produto6 = new Document();
		produto6.append("nome", "Prod6");
		produto6.append("descricao", "Bla bla");
		produto6.append("valor", "1500.0");
		produto6.append("estado", "Bla bla");
		
		List<Document> listaProdutos = Arrays.asList(produto1, produto2, produto3, 
				produto4, produto5, produto6);
		colection.insertMany(listaProdutos);
		
		return colection;
	}
	
	public static MongoDatabase conectarMongoDB(String baseName, String url) {
		MongoDatabase db = null;
	    try {
	    	MongoClient client = MongoClients.create(url);   	
	    	db = client.getDatabase(baseName);
	    	
		} catch (Exception e) {
			System.out.println("Erro de Conexão :" + e);
			e.printStackTrace();
		}
	    
		return db;				
	}
	
	public void fecharConexao() {
		if (client != null) {
			client.close();
		}
			
	}
	
	public void listaProdutosMongoDB(MongoCollection<Document> colection) {
		
		Iterable<Document> produtos = colection.find();
		for (Document produto : produtos) {
			String nome = produto.getString("nome");
			String descricao = produto.getString("descricao");
			String valor = produto.getString("valor");
			String estado =  produto.getString("estado");
			System.out.println(nome + " -- " + descricao + " -- " + valor + " -- " + estado);
		}
	}
	
	public void insereProdutoMongoDB(MongoCollection<Document> colection, String name, String descricao, String valor, String estado) {
		Document produtos = new Document();
		produtos.append("nome", name);
		produtos.append("descricao", descricao);
		produtos.append("valor", valor);
		produtos.append("estado", estado);
		
		colection.insertOne(produtos);
	}

	public void alteraValorProdutoMongoDB(MongoCollection<Document> colection, String name, String valor) {
		BasicDBObject produto = new BasicDBObject();
		produto.append("nome", name);
		
		BasicDBObject novoValor = new BasicDBObject();
		novoValor.append("$set", new BasicDBObject().append("valor", valor));

		colection.updateMany(produto, novoValor);
	}
	
	public void apagaProdutoMongoDB(MongoCollection<Document> colection, String name) {
		
		colection.deleteOne(Filters.eq("nome", name));
	}
}
