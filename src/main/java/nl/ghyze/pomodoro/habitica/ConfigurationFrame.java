package nl.ghyze.pomodoro.habitica;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import org.apache.commons.lang3.StringUtils;

import nl.ghyze.pomodoro.model.Settings;

public class ConfigurationFrame extends JFrame
{
   private final class TaskListUpdater implements ActionListener
   {
      @Override
      public void actionPerformed(ActionEvent arg0)
      {
         updateTaskList();
      }

      private void updateTaskList()
      {
         if (StringUtils.isEmpty(userId.getText()) || StringUtils.isEmpty(apiToken.getText()))
         {
            return;
         }

         HabiticaService service = new HabiticaService(userId.getText(), apiToken.getText());
         List<HabiticaTask> tasks = service.getTasks(new HabitFilter());

         taskList.removeAllItems();
         for (HabiticaTask task : tasks)
         {
            taskList.addItem(task);
         }
      }
   }

   private static int LABEL_WIDTH = 200;
   private SpringLayout layout = new SpringLayout();

   private JTextField userId = new JTextField();
   private JTextField apiToken = new JTextField();
   private JComboBox<HabiticaTask> taskList = new JComboBox<HabiticaTask>();

   private JButton btOk = new JButton("OK");
   private JButton btCancel = new JButton("Cancel");
   private JButton btCheckTasks = new JButton("Check Tasks");

   private Settings settings;

   public ConfigurationFrame(Settings settings)
   {
      this.settings = settings;
      this.setPreferredSize(new Dimension(400, 400));

      this.getContentPane().setLayout(layout);
      init();
      initTaskList();
      initButtons();
      pack();
   }

   private void initTaskList()
   {
      new TaskListUpdater().updateTaskList();
      int maxIndex = taskList.getItemCount();
      for (int i = 0; i < maxIndex; i++)
      {
         HabiticaTask task = taskList.getItemAt(i);
         if (task.getId().equals(settings.getHabiticaTaskId()))
         {
            taskList.setSelectedIndex(i);
         }
      }
   }

   private void init()
   {
      JLabel lbUserId = new JLabel("User ID");
      layout.putConstraint(SpringLayout.WEST, lbUserId, 5, SpringLayout.WEST, getContentPane());
      layout.putConstraint(SpringLayout.EAST, lbUserId, LABEL_WIDTH, SpringLayout.WEST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, lbUserId, 5, SpringLayout.NORTH, getContentPane());
      layout.putConstraint(SpringLayout.SOUTH, lbUserId, 25, SpringLayout.NORTH, getContentPane());
      this.add(lbUserId);

      layout.putConstraint(SpringLayout.WEST, userId, 5, SpringLayout.EAST, lbUserId);
      layout.putConstraint(SpringLayout.EAST, userId, -5, SpringLayout.EAST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, userId, 5, SpringLayout.NORTH, getContentPane());
      layout.putConstraint(SpringLayout.SOUTH, userId, 25, SpringLayout.NORTH, getContentPane());
      this.add(userId);
      userId.setText(settings.getHabiticaUser());

      JLabel lbApiToken = new JLabel("API Token");
      layout.putConstraint(SpringLayout.WEST, lbApiToken, 5, SpringLayout.WEST, getContentPane());
      layout.putConstraint(SpringLayout.EAST, lbApiToken, LABEL_WIDTH, SpringLayout.WEST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, lbApiToken, 5, SpringLayout.SOUTH, lbUserId);
      layout.putConstraint(SpringLayout.SOUTH, lbApiToken, 25, SpringLayout.SOUTH, lbUserId);
      this.add(lbApiToken);

      layout.putConstraint(SpringLayout.WEST, apiToken, 5, SpringLayout.EAST, lbApiToken);
      layout.putConstraint(SpringLayout.EAST, apiToken, -5, SpringLayout.EAST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, apiToken, 5, SpringLayout.SOUTH, lbUserId);
      layout.putConstraint(SpringLayout.SOUTH, apiToken, 25, SpringLayout.SOUTH, lbUserId);
      this.add(apiToken);
      apiToken.setText(settings.getHabiticaApi());

      JLabel lbTasks = new JLabel("Choose task");
      layout.putConstraint(SpringLayout.WEST, lbTasks, 5, SpringLayout.WEST, getContentPane());
      layout.putConstraint(SpringLayout.EAST, lbTasks, LABEL_WIDTH, SpringLayout.WEST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, lbTasks, 5, SpringLayout.SOUTH, lbApiToken);
      layout.putConstraint(SpringLayout.SOUTH, lbTasks, 25, SpringLayout.SOUTH, lbApiToken);
      this.add(lbTasks);

      layout.putConstraint(SpringLayout.WEST, taskList, 5, SpringLayout.EAST, lbTasks);
      layout.putConstraint(SpringLayout.EAST, taskList, -5, SpringLayout.EAST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, taskList, 5, SpringLayout.SOUTH, lbApiToken);
      layout.putConstraint(SpringLayout.SOUTH, taskList, 25, SpringLayout.SOUTH, lbApiToken);
      this.add(taskList);

      layout.putConstraint(SpringLayout.WEST, btCheckTasks, 5, SpringLayout.EAST, lbTasks);
      layout.putConstraint(SpringLayout.EAST, btCheckTasks, -5, SpringLayout.EAST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, btCheckTasks, 5, SpringLayout.SOUTH, taskList);
      layout.putConstraint(SpringLayout.SOUTH, btCheckTasks, 25, SpringLayout.SOUTH, taskList);
      this.add(btCheckTasks);

      btCheckTasks.addActionListener(new TaskListUpdater());

   }

   private void initButtons()
   {
      layout.putConstraint(SpringLayout.WEST, btCancel, -105, SpringLayout.EAST, getContentPane());
      layout.putConstraint(SpringLayout.EAST, btCancel, -5, SpringLayout.EAST, getContentPane());
      layout.putConstraint(SpringLayout.NORTH, btCancel, -25, SpringLayout.SOUTH, getContentPane());
      layout.putConstraint(SpringLayout.SOUTH, btCancel, -5, SpringLayout.SOUTH, getContentPane());
      this.getContentPane().add(btCancel);

      layout.putConstraint(SpringLayout.WEST, btOk, -105, SpringLayout.WEST, btCancel);
      layout.putConstraint(SpringLayout.EAST, btOk, -5, SpringLayout.WEST, btCancel);
      layout.putConstraint(SpringLayout.NORTH, btOk, -25, SpringLayout.SOUTH, getContentPane());
      layout.putConstraint(SpringLayout.SOUTH, btOk, -5, SpringLayout.SOUTH, getContentPane());
      this.getContentPane().add(btOk);

      btCancel.addActionListener(new ActionListener()
         {

            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               setVisible(false);
            }

         });

      btOk.addActionListener(new ActionListener()
         {
            @Override
            public void actionPerformed(ActionEvent arg0)
            {
               updateSettings();
               setVisible(false);
            }
         });
   }

   private void updateSettings()
   {
      if (settings != null)
      {
         settings.setHabiticaUser(userId.getText());
         settings.setHabiticaApi(apiToken.getText());
         HabiticaTask selectedTask = (HabiticaTask) taskList.getSelectedItem();
         settings.setHabiticaTaskId(selectedTask.getId());
      }
   }

}
