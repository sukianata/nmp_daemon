 
namespace java com.fiberhome.thrift

struct Board{
	1:i32 BoardId;
	2:i32 ProjectId;
	3:i32 NeId;
	4:i32 GroupId;
	5:i32 GroupNo;
	6:string GroupName;
	7:i32 BureauId;
	8:i32 BureauNo;
	9:string BureauName;
	10:i32 RackId;
	11:i32 RackNo;
	12:string RackName;
	13:i32 ShelfId;
	14:string ShelfName;
	15:i32 SlotId;
	16:i32 SlotNo;
	17:i32 SlotAddress;
	18:string SlotName;
	19:i32 BoardNo;
	20:i32 BoardType;
	21:string BoardAlias;
	22:string BoardName;
	23:string BoardNamec; 
	24:i32 ShelfNo;
	25:i32 ShelfType;
}

struct NE{
	1:i32 NeId;
	2:i32 PjtId;
	3:i32 PartId;
	4:string PartName;
	5:i32 ManagerId;
	6:i32 PartType;
	7:string NeName;
	8:string LocationName;
	9:i32 NeTypeNo;
	10:string NeTypeName;
	11:i32 EMUType;
	12:i32 PartNo;
	13:i32 NeNo;
	14:string NeIp;
	15:i32 NeSwitch1;
	16:i32 NeSwitch2;
	17:i32 ManagerA;
	18:i32 ManagerB;
	19:string CreateTime;
	20:i32 LoginDomainId;
	21:string LoginDomainName;
	22:string PjtName;
	23:string BasicDomainMask;
	24:string BasicDomainid;
	25:string BasicDomainType;
}

struct TopoNode{
	1:i32 TopoNodeId;
	2:i32 ProjectId;
	3:i32 NeId;
	4:i32 ParentId;
	5:i32 TopoNodeType;
	6:i32 PosX;
	7:i32 PosY;
	8:i32 PosZ;
	9:i32 Layer3D;
}

struct TopoLine{
	1:i32 TopoLinkId;
	2:i32 ProjectId;
	3:i32 TopoNodeId1;
	4:i32 BoardId1;
	5:string PortName1;
	6:i32 PortX1;
	7:i32 PortY1;
	8:i32 PortZ1;
	9:i32 TopoNodeId2;
	10:i32 BoardId2;
	11:string PortName2;
	12:i32 PortX2;
	13:i32 PortY2;
	14:i32 PortZ2;
	15:i32 LinkNo;
	16:i32 LinkSpeedNo;
	17:string LinkSpeedName;
	18:string LinkDrawParam;
	19:string TopoLinkType;
	20:string PortKey1;
	21:string PortKey2;
}

struct Topology{
	1:list<TopoLine> Lines;
	2:list<TopoNode> Nodes;
}

struct Port{
	1:i32 BoardId;
	2:string BoardName;
	3:string PortSpeed;
	4:i32 Direction;
	5:i32 Mode;
	6:i32 PortType;
	7:string PortName;
	8:i32 PortGroupType;
	9:string PortGroupName;
	10:i32 IsSignedPort;
	11:i32 PortInOut;
	12:i32 TribPortNumber;
	13:i32 StartStm1No;
	14:i32 StartTribNo;
	15:i32 StartLineNo;
	16:string PortLineNo;
	17:i32 PortNo;
	18:string PortKey;
}
 

enum ResourceEnum{
	None=0x00,
	Board=0x01,
	NE=0x02,
	Topology=0x04,
	Port=0x08
}

struct Summary{
	1:list<Board> Boards;
	2:list<NE> NEs;
	3:Topology Topologies;
	4:list<Port> Ports;	 
}
  

enum NmType{
    O2000,
    U2000_TRANSFER,
    U2000_ACESS
}


struct NMPInfo{
	1:string NmMemberType;
	2:string NmMemberMainVersion;
	3:string NmMemberPackVersion;
	4:string IP;
	5:i32 Port;
	6:string NodeName;
	7:string InstallPath;
	8:string SchemaInfo_Name;
	9:string SchemaInfo_User;
	10:string SchemaInfo_Password;
	11:string DatabaseType;
	12:string DatabaseVersion;
	13:string DbInstallPath;
}
 
//CPU_Percentage，最大为100，最小为0
struct SystemStatus{
	1:double CPU_Percentage; 
	2:i64 MemoryCapacity_Used;
	3:i64 MemoryCapacity_Total;
	4:i64 DiskCapacity_Used;
	5:i64 DiskCapacity_Total;
	6:double CurrentProcCPU;
	7:list<DiskInfo> DiskInfos;
}

struct DiskInfo{
	1:string Name;
	2:string Description;
	3:i64 TotalSize;
	4:i64 FreeSize;
}
 
struct ProcStatus{
	1:double ProcCPU;
	2:double MemoryCost;
	3:string Path;
}