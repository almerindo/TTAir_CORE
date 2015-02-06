package org.ttair;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;




public abstract class TTAirControl implements Serializable, TTAirIControl{

	private static final long serialVersionUID = -2960072578429727409L;
	private List<TTAirObject> list = new ArrayList<TTAirObject>();
	//private TTAirObject objAtual = null;
	private int indexObjAtual = -1;
	private boolean log = false;
	
	public void testeAdd(TTAirObject obj) throws Exception{
		this.testObjType(obj); //Se não for do tipo permitido lança exception
		if (obj == null) {
			throw new Exception("Não é possível adicional um OBJ Null");
		}
		
		if (this.exist(obj)) {
			throw new Exception("Objeto já cadastrado");
		}else {
			if (obj.getID()==null){
				throw new Exception("Não é possivel adicionar um Object com ID NULL!");
			}else if (this.existID(obj.getID())){
			  throw new Exception("ID ["+ obj.getID() + "] do Objeto já Existe!");
			}
		}
	}
	
	public List<TTAirObject> getList(){
		return this.list;
	}
	
	public void testeRemove(TTAirObject obj) throws Exception{
		this.testObjType(obj); //Se não for do tipo permitido lança exception
		if(obj==null){
			throw new Exception("Não é possível remover um OBJ Null");
		}
		if (!this.exist(obj)) {
			 throw new Exception("Obj não existe");
		}else {
			if (!this.existID(obj.getID()) ){
				throw new Exception("ID ["+ obj.getID() + "] do Objeto NÃO Existe!");
			}
		}
	}
	/**
	 * Adiciona um objeto. 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser adicionado na lista
	 * @throws Exception se Objeto já existe, se ID já existe, se Obj é null
	 */
	public void add(TTAirObject obj) throws Exception {
		this.testeAdd(obj);
		this.list.add(obj);
		this.indexObjAtual++;
		//this.objAtual = obj;
	}
	
	/**
	 * Adiciona uma lista de objetos. Este método só adiciona 
	 * se TODOS os objetos puderem ser adicionados. Se der erro, nenhum dos objetos serão adicionados
	 * @param listObj Lista de <tt>TTAirObject</tt> a serem adicionados
	 * @throws Exception se objeto já existe, se ID já existe, se Obj é null
	 */
	public void add(List<TTAirObject> listObj) throws Exception{
		//testa todas os objetos antes de adicionar
		for (TTAirObject obj : listObj) {
			this.testeAdd(obj);
		}
		//Se todos passarem no teste adiciona
		for (TTAirObject obj : listObj) {
			this.add(obj);
		}
	}
	
	/**
	 * Remove o objeto do tipo <tt>TTAirObject</tt>. 
	 * @param obj do tipo <tt>TTAirObject</tt> que será removido
	 * @throws Exception se objeto não existe. Se objeto já existe e o ID é diferente
	 */
	public void remove(TTAirObject obj) throws Exception{
		this.testeRemove(obj);
		if (this.exist(obj)) {
			if (this.getObjAtual().equals(obj)) { //Se o que estiver sendo removido , for o atual
				if (!this.back()){ //Tenta apontar o atual para o anterior. Se não conseguir,
					this.next(); //aponta para o proximo.
				}
			}
		}
		this.list.remove(obj);
		
	}
	
	/**
	 * Remove todos os objetos contidos na lista. Este método só remove
	 * se <tt>TODOS os objetos</tt> puderem ser removidos. Se der erro em um dos objetos, nenhum será removido.
	 * @param listObj lista de <tt>TTAirObj</tt> que deverá ser removido
	 * @throws Exception se objeto não existe. Se objeto já existe e o ID é diferente
	 */
	public void remove(List<TTAirObject> listObj) throws Exception{
		//Testa todos os objetos antes de remover
		for (TTAirObject obj : listObj) {
			this.testeRemove(obj);
		}
		//se todos passarem no teste, remove
		for (TTAirObject obj: list) {
			this.remove(obj);	
		}
	}
	
	/**
	 * Método utilizado para saber se não existe objetos a serem controlados
	 * @return <tt>true</tt> se está vazio, <tt>false</tt> se não está vazio
	 */
	public boolean isEmpty(){
		return this.list.isEmpty();
	}
	
	/**
	 * Método utilizado para saber se já existe algum <tt>TTAirObject</tt> 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser vertificado
	 * @return <tt>true</tt> se existe ou <tt>false</tt> se não existe
	 */
	public boolean exist(TTAirObject obj){
		try {
			this.testObjType(obj); //Se não for do tipo permitido retorna false
		} catch (Exception e) {
			return false;
		} 
		return list.contains(obj);
	}
	
	/**
	 * Verifica se um determinado id já existe na lista
	 * @param id do objeto do tipo <tt>TTAirObject</tt>
	 * @return @return <tt>true</tt> se existe ou <tt>false</tt> se não existe
	 */
	public boolean existID(String id){
		for (TTAirObject objAux : this.list) {
			if (objAux.getID().equalsIgnoreCase(id)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * retorna um <tt>TTAirObject</tt> pelo ID passado como parâmetro
	 * @param id do <tt>TTAirObject</tt> a ser pesquisado
	 * @return <tt>TTAirObject</tt>
	 */
	public TTAirObject getByID(String id){
		for (TTAirObject obj : list) {
			if (obj.getID().equalsIgnoreCase(id)){
				return obj;
			}
		}
		return null;
	}


	public boolean first(){
		try {
			this.goTo(0);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean last(){
		try {
			this.goTo(this.list.size()-1);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public void goTo(int index ) throws Exception{
		if (this.isEmpty()){
			throw new Exception("Lista de BehaviorChain vazia!");
		}
		if ((index<0) || (index>= this.list.size()) ){
			throw new Exception("Indice fora da faixa permitida");
		}
		//this.objAtual = this.list.get(index); //atual recebe o proximo
		this.indexObjAtual = index;
		  
	}
	
	public void goTo(TTAirObject obj ) throws Exception{
		if (exist(obj)) {
			int index = this.list.indexOf(obj);
			this.goTo(index);
		}
		
		
	}

		
	
	public boolean next(){
		int index = this.indexObjAtual;
		index = index +1; //incrementa o indice
		try {
			this.goTo(index);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean back(){
		int index = this.indexObjAtual;
		index = index -1; //decrementa o indice
		try {
			this.goTo(index);
		} catch (Exception e) {
			//System.out.println(e.getMessage());
			return false;
		}
		return true;

	}


	public TTAirObject getObjAtual() {
		
		//return objAtual;
		return this.list.get(this.indexObjAtual);
	}


	public boolean isLog() {
		return log;
	}


	public void setLog(boolean log) {		
		for (TTAirObject obj : list) {
			obj.setLog(log);
		}
		this.log = log;
	}
	
	public void clear(){
		this.indexObjAtual = -1;
		this.list.clear();
	}
	
	public abstract void testObjType(TTAirObject obj) throws Exception;

}
