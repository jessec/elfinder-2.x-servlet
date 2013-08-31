package cn.bluejoe.elfinder.controller.executor;

import java.util.Map;

public interface CommandExecutorFactory
{
	CommandExecutor get(String commandName);

	void setClassNamePattern(String string);

	void setMap(Map<String, CommandExecutor> map);
}