package com.Util;

import javax.swing.*;

/**
 * 右键菜单
 */
public class RightClickMenuExample {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Right Click Menu Example");
        JPanel panel = new JPanel();

        // 创建右键菜单
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem menuItem1 = new JMenuItem("菜单项1");
        menuItem1.addActionListener(e -> {
            System.out.println("e = " + e);
        });
        JMenuItem menuItem2 = new JMenuItem("菜单项2");
        JMenuItem menuItem3 = new JMenuItem("菜单项3");
        popupMenu.add(menuItem1);
        popupMenu.add(menuItem2);
        popupMenu.add(menuItem3);

        // 添加鼠标右键监听器
//        panel.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseReleased(MouseEvent e) {
//                if (e.isPopupTrigger()) {
//                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
//                }
//            }
//        });
        panel.setComponentPopupMenu(popupMenu);
        /*
        panel.setComponentPopupMenu(new JPopupMenus().addMenu("菜单项2", new JPopupMenus.JPopupMenusClick() {
            @Override
            public void click(String str) {
                System.out.println("str = " + str);
            }
        }).addMenu("菜单项22", new JPopupMenus.JPopupMenusClick() {
            @Override
            public void click(String str) {
                System.out.println("str = " + str);
            }
        }));*/

        frame.getContentPane().add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}