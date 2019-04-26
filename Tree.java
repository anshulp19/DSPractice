import java.util.*;

public class DS_Trees<T extends Comparable> {
    Node root;

    class Node {
        T value;
        Node left;
        Node right;

        public Node() {}

        public Node(T value) {
            this.value = value;
            this.left = null;
            this.right = null;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }

    private Node insert(Node node, T value) {
        if (node == null) {
            Node temp = new Node(value);
            node = temp;
            return node;
        }
        if (value.compareTo(node.getValue()) <= 0)
            node.setLeft(insert(node.getLeft(), value));
        else
            node.setRight(insert(node.getRight(), value));

        return node;
    }

    public Node getRoot() {
        return root;
    }

    public void insert(T value) {
        root = insert(root, value);
    }

    private void preorder(Node node) {
        if (node != null) {
            System.out.print(node.getValue() + " ");
            preorder(node.getLeft());
            preorder(node.getRight());
        }
    }

    public void preorder() {
        preorder(this.getRoot());
        System.out.println();
    }

    private void postorder(Node node) {
        if (node != null) {
            postorder(node.getLeft());
            postorder(node.getRight());
            System.out.print(node.getValue() + " ");
        }
    }

    public void postorder() {
        postorder(this.getRoot());
        System.out.println();
    }

    private void inorder(Node node) {
        if (node != null) {
            inorder(node.getLeft());
            System.out.print(node.getValue() + " ");
            inorder(node.getRight());
        }
    }

    public void inorder() {
        inorder(this.getRoot());
        System.out.println();
    }

    public void levelorder() {
        Queue<Node> q = new LinkedList<>();
        q.add(this.getRoot());

        while (!q.isEmpty()) {
            Node temp = q.poll();
            if (temp!= null)
                System.out.print(temp.getValue() + " ");
            if (temp.getLeft() != null)
                q.add(temp.getLeft());
            if (temp.getRight() != null)
                q.add(temp.getRight());
        }
        System.out.println();
    }

   private void verticalorder(Node node, int level, TreeMap<Integer, List<T>> m) {
        if (node == null)
            return;

        List<T> temp = m.get(level);
        if (temp == null) {
            temp = new ArrayList<>();
            temp.add(node.getValue());
        } else {
            temp.add(node.getValue());
        }

        m.put(level, temp);

       verticalorder(node.getLeft(), level - 1, m);
       verticalorder(node.getRight(), level + 1, m);
   }

   public void verticaloder() {
        TreeMap<Integer, List<T>> tm = new TreeMap();
        int hd = 0;
        verticalorder(this.getRoot(), hd, tm);

        for (Map.Entry<Integer, List<T>> m: tm.entrySet())
            System.out.println(m.getValue());

   }

    private Node invertTree(Node node) {
        if (node == null)
            return null;

        Node left = invertTree(node.getLeft());
        Node right = invertTree(node.getRight());

        node.setRight(left);
        node.setLeft(right);

        return node;
    }


    public void invertTree() {
        if (this.getRoot() != null)
            root = invertTree(this.getRoot());

        inorder(root);
    }

    private int longestConsecutiveSequence(Node node, Node prevNode, int depth) {
        if (node == null)
            return 0;

        int currentDepth = 0;
        if (prevNode != null && (Integer)prevNode.getValue() + 1 == (Integer) node.getValue())
            currentDepth = depth + 1;
        else
            currentDepth = 1;

        return Math.max(currentDepth, Math.max(longestConsecutiveSequence(node.getLeft(), node, currentDepth),
                longestConsecutiveSequence(node.getRight(), node, currentDepth)));
    }


    public int longestConsecutiveSequence() {
        if (this.getRoot() == null)
            return 0;
        return longestConsecutiveSequence(this.getRoot(), null, 0);
    }

    private boolean isValidBST(Node node) {
        Stack<Node> stack = new Stack<>();
        double inorder = - Double.MAX_VALUE;

        while (!stack.isEmpty() || node != null) {
            while (node != null) {
                stack.push(node);
                node = node.getLeft();
            }

            node = stack.pop();
            if ((Integer)node.getValue() <= inorder)
                return false;
            inorder = (Integer)node.getValue();
            node = node.getRight();
        }

        return true;
    }

    public boolean isValidBST() {
        return this.isValidBST(this.getRoot());
    }

    public static void main(String[] args) {
        DS_Trees<Integer> tree = new DS_Trees<>();
        tree.insert(20);
        tree.insert(8);
        tree.insert(4);
        tree.insert(12);
        tree.insert(10);
        tree.insert(14);
        tree.insert(22);
        tree.insert(25);

        // tree.inorder();
        // tree.levelorder();
        // tree.verticaloder();
        // tree.invertTree();
        // System.out.println(tree.longestConsecutiveSequence());
        // System.out.println(tree.isValidBST());
    }
}
