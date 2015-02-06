package org.ttair;

import java.util.List;

public interface TTAirIControl {
	
	
	
	/**
	 * Adiciona um objeto. 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser adicionado na lista
	 * @throws Exception se Objeto j� existe, se ID j� existe, se Obj � null
	 */
	public void add(TTAirObject obj) throws Exception ;
	
	/**
	 * Adiciona uma lista de objetos. Este m�todo s� adiciona 
	 * se TODOS os objetos puderem ser adicionados. Se der erro, nenhum dos objetos ser�o adicionados
	 * @param listObj Lista de <tt>TTAirObject</tt> a serem adicionados
	 * @throws Exception se objeto j� existe, se ID j� existe, se Obj � null
	 */
	public void add(List<TTAirObject> listObj) throws Exception;
	
	/**
	 * Remove o objeto do tipo <tt>TTAirObject</tt>. 
	 * @param obj do tipo <tt>TTAirObject</tt> que ser� removido
	 * @throws Exception se objeto n�o existe. Se objeto j� existe e o ID � diferente
	 */
	public void remove(TTAirObject obj) throws Exception;
	
	/**
	 * Remove todos os objetos contidos na lista. Este m�todo s� remove
	 * se <tt>TODOS os objetos</tt> puderem ser removidos. Se der erro em um dos objetos, nenhum ser� removido.
	 * @param listObj lista de <tt>TTAirObj</tt> que dever� ser removido
	 * @throws Exception se objeto n�o existe. Se objeto j� existe e o ID � diferente
	 */
	public void remove(List<TTAirObject> listObj) throws Exception;
	
	/**
	 * M�todo utilizado para saber se n�o existe objetos a serem controlados
	 * @return <tt>true</tt> se est� vazio, <tt>false</tt> se n�o est� vazio
	 */
	public boolean isEmpty();
	
	/**
	 * M�todo utilizado para saber se j� existe algum <tt>TTAirObject</tt> 
	 * @param obj do tipo <tt>TTAirObject</tt> a ser vertificado
	 * @return <tt>true</tt> se existe ou <tt>false</tt> se n�o existe
	 */
	public boolean exist(TTAirObject obj);
	
	/**
	 * Verifica se um determinado id j� existe na lista
	 * @param id do objeto do tipo <tt>TTAirObject</tt>
	 * @return @return <tt>true</tt> se existe ou <tt>false</tt> se n�o existe
	 */
	public boolean existID(String id);
	
	/**
	 * retorna um <tt>TTAirObject</tt> pelo ID passado como par�metro
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
