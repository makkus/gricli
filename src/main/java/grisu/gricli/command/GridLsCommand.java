package grisu.gricli.command;

import grisu.control.ServiceInterface;
import grisu.control.exceptions.RemoteFileSystemException;
import grisu.gricli.GricliEnvironment;
import grisu.gricli.GricliRuntimeException;
import grisu.gricli.completors.FqanCompletor;
import grisu.model.dto.GridFile;
import jline.ANSIBuffer;


public class GridLsCommand implements
GricliCommand {


	private final String path;

	@SyntaxDescription(command={"gls"},arguments={"path"})
	@AutoComplete(completors={FqanCompletor.class})
	public GridLsCommand(String path){
		this.path = path;
	}

	public GricliEnvironment execute(GricliEnvironment env)
	throws GricliRuntimeException {
		ServiceInterface si = env.getServiceInterface();
		try {
			String url = "grid://groups" + this.path;
			GridFile folder = si.ls(url, 1);
			for (GridFile file : folder.getChildren()) {
				String result = null;
				if (file.isFolder()) {
					result = file.getName() + "/";
				} else {
					result = file.getName();
				}
				if (file.isVirtual()){
					result = new ANSIBuffer().red(result).toString();
				}
				env.printMessage(result);
			}

		} catch (RemoteFileSystemException ex) {
			throw new GricliRuntimeException(ex);
		}
		return env;
	}

}
