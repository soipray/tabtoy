// Generated by github.com/davyxu/tabtoy
// DO NOT EDIT!!
// Version: 2.9.3
package main;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Table {	
	 public enum ActorType { 
			None(0), //  
			Pharah(1), // 法鸡 
			Junkrat(2), // 狂鼠 
			Genji(3), // 源氏 
			Mercy(4), // 天使 
		;
       ActorType(int v) {
           this.ActorType = v;
       }
	   public int getActorType() {
           return ActorType;
       }
       private final int ActorType;
	 }
	
	
	public class ExampleData { 	
		public int ID = 0; // 任务ID; 	
		public String Name = ""; // 名称; 	
		public float Rate = 0; // 比例; 	
		public ActorType Type = ActorType.None; // 类型; 	
		public int[] Skill = new int[]{}; // 技能列表; 	
		public int Buff = 0; // 增益; 	
		public String[] TagList = new String[]{}; // 标记; 
	}
	
	public class ExtendData { 	
		public float Additive = 0; // 附加; 
	}
	
	public class ExampleKV { 	
		public String ServerIP = ""; // 服务器IP; 	
		public int ServerPort = 0; // 服务器端口; 	
		public int[] GroupID = new int[]{}; // 分组; 
	}
	
	
	public List<ExampleData> ExampleData = new ArrayList<>(); // table: ExampleData 
	public List<ExtendData> ExtendData = new ArrayList<>(); // table: ExtendData 
	public List<ExampleKV> ExampleKV = new ArrayList<>(); // table: ExampleKV 

	// Indices 
	public Map<Integer, ExampleData> ExampleDataByID = new HashMap<Integer, ExampleData>(); // table: ExampleData 
	
	// table: ExampleKV
	public ExampleKV GetKeyValue_ExampleKV() {
		return ExampleKV.get(0);
	}
	
	public interface TableEvent{
		void OnPreProcess( );
		void OnPostProcess( );
	}
	// Handlers
	private List<TableEvent> eventHandlers = new ArrayList<TableEvent>();

	// 注册加载后回调
	public void RegisterEventHandler(TableEvent ev ){
		eventHandlers.add(ev);
	}

	// 清除索引和数据, 在处理前调用OnPostProcess, 可能抛出异常
	public void ResetData()  {

		for( TableEvent ev : eventHandlers){
			ev.OnPreProcess();
		}
		
		ExampleData.clear(); 
		ExtendData.clear(); 
		ExampleKV.clear(); 
		
		ExampleDataByID.clear(); 	
	}

	// 构建索引, 需要捕获OnPostProcess可能抛出的异常
	public void  BuildData()  {
		
		for( ExampleData v:ExampleData ) {
			ExampleDataByID.put(v.ID, v);
		}

		for( TableEvent ev : eventHandlers){
			ev.OnPostProcess();
		}
	}
}

