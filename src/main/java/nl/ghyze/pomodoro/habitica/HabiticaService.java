package nl.ghyze.pomodoro.habitica;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class HabiticaService
{
   private String userId;
   private String apiToken;

   public HabiticaService(String userId, String apiToken)
   {
      this.userId = userId;
      this.apiToken = apiToken;
   }

   public List<HabiticaTask> getTasks(HabiticaTaskFilter filter)
   {
      HabiticaTask[] taskArray = getTaskArray();

      List<HabiticaTask> tasks = filterTasks(filter, taskArray);

      return tasks;
   }

   public void updateTask(String taskId, boolean up)
   {
      String direction = up ? "up" : "down";
      try
      {
         String result = doRequest("/user/tasks/" + taskId + "/" + direction, "POST");
         System.out.println(result);
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }

   private HabiticaTask[] getTaskArray()
   {
      String tasksGson = requestTasks();

      Gson gson = new Gson();
      HabiticaTask[] taskArray = gson.fromJson(tasksGson, HabiticaTask[].class);
      return taskArray;
   }

   private List<HabiticaTask> filterTasks(HabiticaTaskFilter filter, HabiticaTask[] taskArray)
   {
      List<HabiticaTask> tasks = new ArrayList<HabiticaTask>();
      for (HabiticaTask task : taskArray)
      {
         if (filter.accept(task))
         {
            tasks.add(task);
         }
      }
      return tasks;
   }

   private String requestTasks()
   {
      try
      {
         String tasks = doRequest("/user/tasks", "GET");
         //         System.out.println(tasks);
         return tasks;
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return "";
   }

   private String doRequest(String path, String method) throws Exception
   {
      URL url = new URL("https://habitica.com/api/v2" + path);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.addRequestProperty("x-api-user", userId);
      connection.addRequestProperty("x-api-key", apiToken);
      connection.setRequestMethod(method);
      connection.connect();
      InputStream is = connection.getInputStream();
      InputStreamReader reader = new InputStreamReader(is);
      char[] characters = new char[256];
      StringBuilder builder = new StringBuilder();
      int i = reader.read(characters);
      while (i >= 0)
      {
         builder.append(characters, 0, i);
         i = reader.read(characters);
      }
      return builder.toString();
   }
}
