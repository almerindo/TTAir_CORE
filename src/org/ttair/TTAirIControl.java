package org.ttair;

import java.util.List;

public interface TTAirIControl {
	
	
	
	/**
	 * Adiciona um objeto. 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser adicionado na lista
	 * @throws Exception se Objeto já existe, se ID já existe, se Obj é null
	 */
	public void add(TTAirObject obj) throws Exception ;
	
	/**
	 * Adiciona uma lista de objetos. Este método só adiciona 
	 * se TODOS os objetos puderem ser adicionados. Se der erro, nenhum dos objetos serão adicionados
	 * @param listObj Lista de <tt>TTAirObject</tt> a serem adicionados
	 * @throws Exception se objeto já existe, se ID já existe, se Obj é null
	 */
	public void add(List<TTAirObject> listObj) throws Exception;
	
	/**
	 * Remove o objeto do tipo <tt>TTAirObject</tt>. 
	 * @param obj do tipo <tt>TTAirObject</tt> que será removido
	 * @throws Exception se objeto não existe. Se objeto já existe e o ID é diferente
	 */
	public void remove(TTAirObject obj) throws Exception;
	
	/**
	 * Remove todos os objetos contidos na lista. Este método só remove
	 * se <tt>TODOS os objetos</tt> puderem ser removidos. Se der erro em um dos objetos, nenhum será removido.
	 * @param listObj lista de <tt>TTAirObj</tt> que deverá ser removido
	 * @throws Exception se objeto não existe. Se objeto já existe e o ID é diferente
	 */
	public void remove(List<TTAirObject> listObj) throws Exception;
	
	/**
	 * Método utilizado para saber se não existe objetos a serem controlados
	 * @return <tt>true</tt> se está vazio, <tt>false</tt> se não está vazio
	 */
	public boolean isEmpty();
	
	/**
	 * Método utilizado para saber se já existe algum <tt>TTAirObject</tt> 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser vertificado
	 * @return <tt>true</tt> se existe ou <tt>false</tt> se não existe
	 */
	public boolean exist(TTAirObject obj);
	
	/**
	 * Verifica se um determinado id já existe na lista
	 * @param id do objeto do tipo <tt>TTAirObject</tt>
	 * @return @return <tt>true</tt> se existe ou <tt>false</tt> se não existe
	 */
	public boolean existID(String id);
	
	/**
	 * retorna um <tt>TTAirObject</tt> pelo ID passado como parâmetro
	 * @param id do <tt>TTAirObject</tt> a ser pesquisado
	 * @return <tt>TTAirObject</tt>
	 */
	public TTAirObject getByID(String id);


	public boolean first();

	public boolean last();
	
	public void goTo(int index ) throws Exception;
	
	public boolean next();
	
	public boolean back();


	public TTAirObject getObjAtual();


	public boolean isLog();


	public void setLog(boolean log);

}
