package org.ttair.util;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import org.ttair.util.xml.XMLTypeAction;
import org.ttair.util.xml.XMLTypeBehavior;
import org.ttair.util.xml.XMLTypeBehaviorChain;
import org.ttair.util.xml.XMLTypeBehaviorFrame;
import org.ttair.util.xml.XMLTypeExpectancy;
import org.ttair.util.xml.XMLTypeExpectancyTransition;
import org.ttair.util.xml.XMLTypeInteraction;
import org.ttair.util.xml.XMLTypeInteractionEvent;

public class TTAirXmlToGraphviz {
	TTAirXML ttairXml = TTAirXML.getINSTANCE();
	XMLTypeBehavior xmlBehavior = null;
	GraphViz gv = new GraphViz();

	String pathNewDotFile = "./temp/sample/temp.dot";
	String pathFile = null;
	private static String ident = "	";



	/**
	 * 
	 * @param source se == null é utilizado o getFilePath() para carregar o XMLBehavior
	 * @param subGraphBC Se == True cria o subgraph BehaviorChain
	 * @param subGraphBuss se == True cria o subGraph dos barramentos
	 * @throws Exception
	 */
	private  void getImageByTTAirXml(String source, boolean subGraphBC, boolean subGraphBuss ) throws Exception {
		try {
			if (source==null){
				if (this.pathFile==null) {
					throw new Exception("Caminho do arquivo null. Utilize o método setPathFile(String pathFile) primeiro!");
				}
				xmlBehavior = ttairXml.loader(pathFile);
			}else {
				xmlBehavior = ttairXml.loaderXML(source);
			}
			
			this.generateGraph(subGraphBC, subGraphBuss);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

	private void generateGraph(boolean subGraphBC, boolean subGraphBuss) throws Exception{
		String idBehavior = xmlBehavior.getID();
		if (idBehavior==null){
			idBehavior="TTAirBehavior";
		}else if (idBehavior.isEmpty()){
			idBehavior="TTAirBehavior";
		}
		gv.addln(gv.start_digraph(idBehavior));
		gv.addln(ident + "node [shape=record];");

		if (subGraphBC){
		  createListSubGraphBC(); //Cria a lista de BehaviorChain
		}
		if (subGraphBuss){
			this.createSubGraphBuss();
		}

		//Fecha o Graph
		gv.addln(gv.end_digraph());
		System.out.println(gv.getDotSource());
	}
	
	
	private void createSubGraphBuss(){
		gv.addln(ident+"subgraph cluster_Buss{");
		createListBF();
		createListINT();
		createListACT();
		createBFEdges();
		gv.addln(ident+"};");
	}
	
 	private void createBFEdges() {
		List<XMLTypeBehaviorFrame>listxmlBF =  xmlBehavior.getListBehaviorFrame();
		for (int i = 0; i < listxmlBF.size(); i++) {
			XMLTypeBehaviorFrame xmlBF = listxmlBF.get(i);
			XMLTypeInteractionEvent evt = xmlBF.getEvent();
			if (evt==null){
				LoggerManager.log(Level.SEVERE, "Behaviorframe: "+ xmlBF.getID() + " Possui evento null");
			}else {
				String recID = evt.getIdRecognizer();
				if (recID==null){
					LoggerManager.log(Level.SEVERE, "Behaviorframe: "+ xmlBF.getID() + " Possui evento " + evt.getID() + " Com Recognizer Null");
				}else {
					String line = xmlBF.getID() + ":"+ recID.toLowerCase()+"->INT:" +recID.toLowerCase()+";";
					gv.addln(ident+ident+line);
				}
			}
		}
		
		for (int i = 0; i < listxmlBF.size(); i++) {
			XMLTypeBehaviorFrame xmlBF = listxmlBF.get(i);
			List<String> listXmlAct = xmlBF.getListActionID();
			
			for (int j = 0; j < listXmlAct.size(); j++) {
				String line = xmlBF.getID()+":";
				String xmlAct = listXmlAct.get(j);
				line = line + xmlAct.toLowerCase() +"->ACT :"+ xmlAct.toLowerCase()+";";
				gv.addln(ident+ident+line);
			}
			
		}
		
	}

	private void createListACT() {
		gv.addln(ident+ident+"subgraph cluster_ACT_BUSS{");
		gv.addln(ident+ident+ident+"node [style=filled];");
		gv.addln(ident+ident+ident+"style=filled;");	
		gv.addln(ident+ident+ident+"color=lightblue;");	
		List<XMLTypeAction> listxmlACT = xmlBehavior.getListAction();
		String line = "ACT[shape=record,label=\"<actbus> ACTBUS|<f0>.|";
		for (int i = 0; i < listxmlACT.size(); i++) {
			XMLTypeAction xmlACT = listxmlACT.get(i);
			
			line = line +"<" +xmlACT.getID().toLowerCase()+ ">" + xmlACT.getID(); 
			if (i+1==listxmlACT.size()){
				line=line+"\"];";
			}else{
				line=line+"|";
			}
		}
		
		gv.addln(ident+ident+ident+line);
		
		gv.addln(ident+ident+ident+"label=\"Action Buss\";");
		gv.addln(ident+ident+"}");
		
	}

	private void createListINT() {
		gv.addln(ident+ident+"subgraph cluster_INT_BUSS{");
		gv.addln(ident+ident+ident+"node [style=filled];");
		gv.addln(ident+ident+ident+"style=filled;");	
		gv.addln(ident+ident+ident+"color=lightblue;");	
		List<XMLTypeInteraction> listXmlInt = xmlBehavior.getListInteraction();

		String line = "INT[shape=record,label=\"<intbus>INTBUS|<f0>.|";
		for (int i = 0; i < listXmlInt.size(); i++) {
			XMLTypeInteraction xmlInte = listXmlInt.get(i);
			line = line + "<" +xmlInte.getID().toLowerCase()+">"+xmlInte.getID();
			if(i+1==listXmlInt.size()){
				line = line + "\"];";
			}else{
				line=line+"|";
			}
		}
		/*BARRAMENTO DE RECOGNIZERS*/
		gv.addln(ident+ident+ident+line);	
		gv.addln(ident+ident+ident+"label=\"Interaction Buss\";");	
		gv.addln(ident+ident+"}");	
		
	}

	private void createListBF() {

		gv.addln(ident+ident+"subgraph cluster_BF_Buss{");
		gv.addln(ident+ident+ident+"node [style=filled];");
		gv.addln(ident+ident+ident+"style=filled;");
		gv.addln(ident+ident+ident+"color=lightyellow;");
		List<XMLTypeBehaviorFrame> listBF = xmlBehavior.getListBehaviorFrame();
		for (int i = 0; i < listBF.size(); i++) {
			XMLTypeBehaviorFrame bf = listBF.get(i);
			XMLTypeInteractionEvent evt = bf.getEvent();
			if (evt==null) {
				LoggerManager.log(Level.SEVERE, "BehaviorFrame: "+ bf.getID() +"Não possui um evento");
			}else {
				String idRec = evt.getIdRecognizer();

				if(idRec==null){
					LoggerManager.log(Level.SEVERE, "BehaviorFrame: "+ bf.getID() +" Possui um Evento: "+ evt.getID()+" ID Recognizer null");
				}else {
					String line = bf.getID()+"[shape=record,label=\"<" + bf.getID().toLowerCase()+">"+bf.getID()+"|"+"<f0>.|" +
							"{<"+idRec.toLowerCase()+">" + idRec +"|{";
					List<String> listAct = bf.getListActionID();
					for (int j = 0; j < listAct.size(); j++) {
						String act = listAct.get(j);
						line = line + "<" + act.toLowerCase() +">" + act ;
						if(j+1==listAct.size()){ //Se for a ultim ação
							line=line+"}}\"];"; //Fim BehaviorFrame
							gv.addln(ident+ident+ident+line);
						}else{
							line=line+"|";
						}
					}
				}

			}


		}


		gv.addln(ident+ident+ident+"label=\"BehaviorFrame Buss\";");
		gv.addln(ident+ident+"}");

	}

	private void createListSubGraphBC() throws Exception {

		List<XMLTypeBehaviorChain> listBC = xmlBehavior.getListBehaviorChain();
		for (int i = 0; i < listBC.size(); i++) {
			XMLTypeBehaviorChain bc = listBC.get(i);
			createBCSubGraph(bc);

		}
	}

	private void createBCSubGraph(XMLTypeBehaviorChain bc ) throws Exception {

		gv.addln(ident + "subgraph cluster_" +bc.getID()+ " {");
		gv.addln(ident+ ident + "node [style=filled]; ");
		gv.addln(ident+ ident + "style=filled;");
		gv.addln(ident+ ident + "color=lightblue;");

		/*gv.addln(ident+ ident + "ranksep=.75;"); 
		gv.addln(ident+ ident + "size = \"7.5,7.5\";");
		gv.addln(ident+ ident+"{");//ABRE NODE
		gv.addln(ident+ ident + ident+"node [shape=plaintext, fontsize=16];");

		gv.addln(ident+ident+ident+ getExpLevels(bc));
		
		
		gv.addln(ident+ident+"}"); //FECHA NODE */

		createExpNodes(bc);
		gv.addln(ident + "}"); //FIM SUBGRAPH

	}

	private void createExpNodes(XMLTypeBehaviorChain bc ) throws Exception {
		List<String> listExpID = bc.getExpectanciesId();
		for (int i = 0; i < listExpID.size(); i++) {
			String expID = listExpID.get(i);
			String expGraph = expID +"[shape=record,label=\"<"+expID.toLowerCase()+">"+expID+"|<f0>.|";
			XMLTypeExpectancy exp = xmlBehavior.getExpByID(expID);
			if(exp==null){
				LoggerManager.log(Level.SEVERE, "Expectancy: " + expID +"Não encontrada no barramento Expectancy" );
			}else{
				List<String> listBFID = exp.getListBehaviorFrameID();
				for (int j = 0; j < listBFID.size(); j++) {
					String bfID = listBFID.get(j);
					expGraph = expGraph + "<"+ bfID.toLowerCase()+">"+bfID;
					if (j+1 == listBFID.size()){
						expGraph = expGraph + "\"];";	 // se for o ultimo fecha
					}else {
						expGraph = expGraph + "|"; //Se não for o ultimo usa | 
					}
				}


			}

			gv.addln(ident+ident+expGraph);
		}

		//createRank(bc);

		createExpectancyTranzitions(bc);

		gv.addln(ident+ident+"label=\"BehaviorChain:"+ bc.getID() +"\";");

	}

	@SuppressWarnings("unused")
	private void createRank(XMLTypeBehaviorChain bc) {
		List<String> listExpID = bc.getExpectanciesId();
		for (int i = 0; i < listExpID.size(); i++) {
			String expID = listExpID.get(i);
			String rankline = "{ rank = same;" + createLabelExpLevel(expID) +";"+expID+"};";

			gv.addln(ident+ident+rankline);	
		}

	}

	private void createExpectancyTranzitions(XMLTypeBehaviorChain bc ) {
		List<XMLTypeExpectancyTransition> listXmlExpTran= bc.getExpectancyTransitions();
		for (int i = 0; i < listXmlExpTran.size(); i++) {
			XMLTypeExpectancyTransition expTran = listXmlExpTran.get(i);



			List<String> listCaused = expTran.getCausedBy();
			for (int j = 0; j < listCaused.size(); j++) {
				String caused = listCaused.get(j);
				String gvline = expTran.getSource()+":"+caused.toLowerCase()+"->"+expTran.getTarget()+"[label=\""+caused+":"
						+ getEventOfBF(caused)+"\", fontsize=10];";
				gv.addln(ident+ident+gvline);
			}
		}

	}

	private String getEventOfBF(String bfID) {
		List<XMLTypeBehaviorFrame> listBF = xmlBehavior.getListBehaviorFrame();
		for (int i = 0; i < listBF.size(); i++) {
			XMLTypeBehaviorFrame bf = listBF.get(i);
			if (bf.getID().equalsIgnoreCase(bfID)){
				XMLTypeInteractionEvent evt = bf.getEvent();
				if (evt!=null){
					return bf.getEvent().getCod();
				}else {
					LoggerManager.log(Level.SEVERE, "BehaviorFrame: "+ bfID +"Não possuir um evento!");
					return null;
				}
			}
		}
		return null;
	}

	@SuppressWarnings("unused")
	private String getExpLevels(XMLTypeBehaviorChain xmlBC) {
		List<String> listExpID = xmlBC.getExpectanciesId();
		String levels = "";
		for (int i = 0; i < listExpID.size(); i++) {
			String expID = listExpID.get(i);
			String label = createLabelExpLevel(expID);
			if (i+1==listExpID.size()){ //Se é o ultimo
				label = label + ";";
			}else{
				label = label + "->";
			}
			levels = levels+ label;
		}


		return levels;
	}

	private String createLabelExpLevel(String expID){
		String label = "level";
		return label + expID;
	}

	private Image createImg(){
		String type = "png";
		byte[] imgBin =  gv.getGraph(gv.getDotSource(), type);
		return Toolkit.getDefaultToolkit().createImage(imgBin); 
	}

	/**
	 * retorna uma imagem do Barramento do BehaviorXML
	 * @param behaviorXml - Pode ser o XML ou o caminho do arquivo XML que deverá ser lido
	 * @return Image com o Graph do Barramento
	 * @throws Exception
	 */
	public Image shortViewBussImgByXML(String behaviorXml) throws Exception{
		getImageByTTAirXml(behaviorXml,false,true);
		return this.createImg();
	}
	
	public Image shortViewBussImgByXMLFile(String pathXml) throws Exception{
		this.setPathFile(pathXml);
		getImageByTTAirXml(null,false,true);
		return this.createImg();
	}

	/**
	 * retorna uma imagem do Cluster BehaviorChain do BehaviorXML
	 * @param behaviorXml - Pode ser o XML ou o caminho do arquivo XML que deverá ser lido
	 * @return Image com o Graph do BehaviorChain
	 * @throws Exception
	 */
	public Image shortViewBCImgByXML(String behaviorXml) throws Exception{
		getImageByTTAirXml(behaviorXml,true,false);
		return this.createImg();
	}

	public Image shortViewBCImgByXMLFile(String pathXml) throws Exception{
		this.setPathFile(pathXml);
		getImageByTTAirXml(null,true,false);
		return this.createImg();
	}

	/**
	 * retorna uma imagem do completa do BehaviorXML
	 * @param behaviorXml - Pode ser o XML ou o caminho do arquivo XML que deverá ser lido
	 * @return Image com o Graph do Barramento e do BehaviorChain
	 * @throws Exception
	 */
	public Image fullViewImgByXML(String behaviorXml) throws Exception{
		getImageByTTAirXml(behaviorXml,true,true);
		return this.createImg(); 
	}
	public Image fullViewImgByXMLFile(String pathXml) throws Exception{
		this.setPathFile(pathXml);
		getImageByTTAirXml(null,true,true);
		return this.createImg(); 
	}


	public String getPathFile() {
		return pathFile;
	}



	public void setPathFile(String pathFile) {
		this.pathFile = pathFile;
	}

}
