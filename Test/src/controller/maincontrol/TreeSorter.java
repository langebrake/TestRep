package controller.maincontrol;

import gui.interactivepane.InteractiveModule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.LinkedList;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;

import plugin.Plugin;
import stdlib.grouping.Grouping;

public class TreeSorter {
	public static void sortTree(DefaultMutableTreeNode root) {
		  @SuppressWarnings("unchecked")
		Enumeration<DefaultMutableTreeNode> e = root.depthFirstEnumeration();
		  while (e.hasMoreElements()) {
		    DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
		    if (!node.isLeaf()) {
		      sort(node);
		    }
		  }
		  
		}
		public static Comparator< DefaultMutableTreeNode> tnc = new Comparator< DefaultMutableTreeNode>() {
		  @Override public int compare(DefaultMutableTreeNode a, DefaultMutableTreeNode b) {
			Object leftObj = a.getUserObject();
			Object rightObj = b.getUserObject();
			if(leftObj instanceof InteractiveModule && rightObj instanceof InteractiveModule){
				Plugin left = ((InteractiveModule) leftObj).getModule().getPlugin();
				Plugin right = ((InteractiveModule) rightObj).getModule().getPlugin();
				if ((left instanceof Grouping.GroupInput || left instanceof Grouping.GroupOutput)){
					return -1;
				} else if ((right instanceof Grouping.GroupInput || right instanceof Grouping.GroupOutput)){
					return -1;
				}else if(left instanceof Grouping && !(right instanceof Grouping)){
					return -1;
				} else if (!(left instanceof Grouping) && (right instanceof Grouping)) {
					return 1;
				}
				
				
			} 
			
			String sa = a.getUserObject().toString();
			String sb = b.getUserObject().toString();
			return (int) Math.signum(((String) sa).compareToIgnoreCase(sb));
		  }
		};
		
		public static void sort(DefaultMutableTreeNode parent) {
			  int n = parent.getChildCount();
			  LinkedList< DefaultMutableTreeNode> children = new LinkedList< DefaultMutableTreeNode>();
			  for (int i = 0; i < n; i++) {
			    children.add((DefaultMutableTreeNode) parent.getChildAt(i));
			  }
			  Collections.sort(children, tnc); 
			  parent.removeAllChildren();
			  for (MutableTreeNode node: children) {
			    parent.add(node);
			    
			  }
			}
}
