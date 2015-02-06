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
		this.testObjType(obj); //Se n�o for do tipo permitido lan�a exception
		if (obj == null) {
			throw new Exception("N�o � poss�vel adicional um OBJ Null");
		}
		
		if (this.exist(obj)) {
			throw new Exception("Objeto j� cadastrado");
		}else {
			if (obj.getID()==null){
				throw new Exception("N�o � possivel adicionar um Object com ID NULL!");
			}else if (this.existID(obj.getID())){
			  throw new Exception("ID ["+ obj.getID() + "] do Objeto j� Existe!");
			}
		}
	}
	
	public List<TTAirObject> getList(){
		return this.list;
	}
	
	public void testeRemove(TTAirObject obj) throws Exception{
		this.testObjType(obj); //Se n�o for do tipo permitido lan�a exception
		if(obj==null){
			throw new Exception("N�o � poss�vel remover um OBJ Null");
		}
		if (!this.exist(obj)) {
			 throw new Exception("Obj n�o existe");
		}else {
			if (!this.existID(obj.getID()) ){
				throw new Exception("ID ["+ obj.getID() + "] do Objeto N�O Existe!");
			}
		}
	}
	/**
	 * Adiciona um objeto. 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser adicionado na lista
	 * @throws Exception se Objeto j� existe, se ID j� existe, se Obj � null
	 */
	public void add(TTAirObject obj) throws Exception {
		this.testeAdd(obj);
		this.list.add(obj);
		this.indexObjAtual++;
		//this.objAtual = obj;
	}
	
	/**
	 * Adiciona uma lista de objetos. Este m�todo s� adiciona 
	 * se TODOS os objetos puderem ser adicionados. Se der erro, nenhum dos objetos ser�o adicionados
	 * @param listObj Lista de <tt>TTAirObject</tt> a serem adicionados
	 * @throws Exception se objeto j� existe, se ID j� existe, se Obj � null
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
	 * @param obj do tipo <tt>TTAirObject</tt> que ser� removido
	 * @throws Exception se objeto n�o existe. Se objeto j� existe e o ID � diferente
	 */
	public void remove(TTAirObject obj) throws Exception{
		this.testeRemove(obj);
		if (this.exist(obj)) {
			if (this.getObjAtual().equals(obj)) { //Se o que estiver sendo removido , for o atual
				if (!this.back()){ //Tenta apontar o atual para o anterior. Se n�o conseguir,
					this.next(); //aponta para o proximo.
				}
			}
		}
		this.list.remove(obj);
		
	}
	
	/**
	 * Remove todos os objetos contidos na lista. Este m�todo s� remove
	 * se <tt>TODOS os objetos</tt> puderem ser removidos. Se der erro em um dos objetos, nenhum ser� removido.
	 * @param listObj lista de <tt>TTAirObj</tt> que dever� ser removido
	 * @throws Exception se objeto n�o existe. Se objeto j� existe e o ID � diferente
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
	 * M�todo utilizado para saber se n�o existe objetos a serem controlados
	 * @return <tt>true</tt> se est� vazio, <tt>false</tt> se n�o est� vazio
	 */
	public boolean isEmpty(){
		return this.list.isEmpty();
	}
	
	/**
	 * M�todo utilizado para saber se j� existe algum <tt>TTAirObject</tt> 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser vertificado
	 * @return <tt>true</tt> se existe ou <tt>false</tt> se n�o existe
	 */
	public boolean exist(TTAirObject obj){
		try {
			this.testObjType(obj); //Se n�o for do tipo permitido retorna false
		} catch (Exception e) {
			return false;
		} 
		return list.contains(obj);
	}
	
	/**
	 * Verifica se um determinado id j� existe na lista
	 * @param id do objeto do tipo <tt>TTAirObject</tt>
	 * @return @return <tt>true</tt> se existe ou <tt>false</tt> se n�o existe
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
	 * retorna um <tt>TTAirObject</tt> pelo ID passado como par�metro
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
