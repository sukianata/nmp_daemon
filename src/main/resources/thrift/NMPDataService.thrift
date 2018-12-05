 
namespace java com.fiberhome.thrift

include "model.thrift"
include "exception.thrift"
service NMPDataService{
    binary DownloadFileFromZip(1:string path,2:string subpath) throws (1:exception.RPCApplicationException err);
	binary DownloadFile(1:string path) throws (1:exception.RPCApplicationException err);
	binary ReadFile(1:string path,2:i32 position,3:i32 length)  throws (1:exception.RPCApplicationException err);
	i32 GetFileSize(1:string path) throws (1:exception.RPCApplicationException err);
	string LockFile(1:string path) throws (1:exception.RPCApplicationException err);
	bool UnlockFile(1:string path) throws (1:exception.RPCApplicationException err);
	list<string> GetChildFolders(1:string folderPath) throws (1:exception.RPCApplicationException err);
	list<string> GetChildFiles(1:string folderPath) throws (1:exception.RPCApplicationException err);
	list<string> SearchFile(1:string searchPattern,2:string folder) throws (1:exception.RPCApplicationException err);
	bool ExistsFolder(1:string path) throws (1:exception.RPCApplicationException err);
	bool ExistsFile(1:string path) throws (1:exception.RPCApplicationException err);
	bool RegisterFolderWatcher(1:string path,2:string filter,3:bool includeSubDirectories,4:string notifyIP,5:i32 notifyPort) throws (1:exception.RPCApplicationException err);
	bool UnRegisterFolderWatcher(1:string path,2:string filter)  throws (1:exception.RPCApplicationException err);

    bool UnRegisterAllFolderWatcher() throws (1:exception.RPCApplicationException err);

	string GetNMPSN() throws(1:exception.RPCApplicationException err);
	bool SetUpgradeAddress(1:string ip) throws(1:exception.RPCApplicationException err);
	model.SystemStatus GetSystemStatus() throws(1:exception.RPCApplicationException err);

	//物理处理器：NumberOfProcessors
	//逻辑核心数：NumberOfLogicalProcessors
	//物理核心数：NumberOfCores
	//CPU电压：CurrentVoltage
	//外部频率：ExtClock
	//二级缓存：L2CacheSize，单位K
	//三级缓存：L2CacheSize，单位K
	//CPU型号：Name
	//CPU当前使用率：LoadPercentage
	//额定频率：MaxClockSpeed，单位MHz
	//当前频率：CurrentClockSpeed，单位MHz
	//地址宽度：AddressWidth
	//数据宽度：DataWidth
	list<map<string,string>> GetCPUInfo() throws(1:exception.RPCApplicationException err);

	//获取当前程序的版本，40或者35
	string GetProgramVersion()  throws(1:exception.RPCApplicationException err);

	list<model.ProcStatus> GetProcStatus(1:string processName) throws (1:exception.RPCApplicationException err);

	bool SetProxyProtectTime(1:string time) throws(1:exception.RPCApplicationException err);

	model.ProcStatus getCurProcSt() throws (1:exception.RPCApplicationException err);
}


 
