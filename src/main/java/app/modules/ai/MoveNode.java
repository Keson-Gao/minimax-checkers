package app.modules.ai;

import app.utils.helper.Point;

public class MoveNode
{
   private Point nodePosition;
   private MoveNode topLeftChild;
   private MoveNode topRightChild;
   private MoveNode bottomLeftChild;
   private MoveNode bottomRightChild;
   private MoveNode parent;

   public MoveNode(Point pos)
   {
       nodePosition = pos;
   }

   public void setPosition(Point pos)
   {
       nodePosition = pos;
   }

   public void setTopLeftChild(MoveNode child)
   {
       topLeftChild = child;
       topLeftChild.setParent(this);
   }

   public void setTopRightChild(MoveNode child)
   {
       topRightChild = child;
       topRightChild.setParent(this);
   }

   public void setBottomLeftChild(MoveNode child)
   {
       bottomLeftChild = child;
       bottomLeftChild.setParent(this);
   }

   public void setBottomRightChild(MoveNode child)
   {
       bottomRightChild = child;
       bottomRightChild.setParent(this);
   }

   public Point getPosition() { return nodePosition; }
   public MoveNode getTopLeftChild() { return topLeftChild; }
   public MoveNode getTopRightChild() { return topRightChild; }
   public MoveNode getBottomLeftChild() { return bottomLeftChild; }
   public MoveNode getBottomRightChild() { return bottomRightChild; }

   private void setParent(MoveNode parent) { this.parent = parent; }
}
