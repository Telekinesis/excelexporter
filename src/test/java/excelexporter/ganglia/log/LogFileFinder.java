package excelexporter.ganglia.log;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.telekinesis.commonclasses.io.ssh.JschConnector;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class LogFileFinder
{
    private static final Pattern pattern = Pattern
	                                         .compile("(attempt)_(\\d+)_(\\d+)_(m|r)_(\\d+)_(\\d+)");
    private final String         host;
    private final String         username;
    private final String         password;

    public LogFileFinder(String host, String username, String password)
    {
	this.host = host;
	this.username = username;
	this.password = password;
    }

    @SuppressWarnings("unchecked")
    public String[] findTasks(String jobLogPath) throws JSchException
    {
	Session session = JschConnector.connect(host, username, password);
	String mapTask = null;
	String reduceTask = null;
	try
	{
	    ChannelSftp channel = JschConnector.getSftpChannel(session);
	    try
	    {
		Vector<ChannelSftp.LsEntry> tasks = channel.ls(jobLogPath);
		for (ChannelSftp.LsEntry task : tasks)
		{
		    String taskName = task.getFilename();
		    Matcher m = pattern.matcher(taskName);
		    if (m.matches())
		    {
			String type = m.group(4);
			int number = Integer.parseInt(m.group(5));
			if (type.equals("m"))
			{
			    if (mapTask == null && number > 10)
				mapTask = taskName;
			}
			else
			{
			    if (reduceTask == null && number > 5 && number <= 50) reduceTask = taskName;
			}
		    }
		}
	    }
	    catch (SftpException e)
	    {
		channel.disconnect();
	    }
	    String mapperLogName = null;
	    String reducerLogName = null;
	    if (mapTask != null)
		mapperLogName = jobLogPath + "/" + mapTask + "/" + "syslog";
	    if (reduceTask != null)
		reducerLogName = jobLogPath + "/" + reduceTask + "/" + "syslog";
	    return new String[] { mapperLogName, reducerLogName };
	}
	finally
	{
	    session.disconnect();
	}
    }

}
