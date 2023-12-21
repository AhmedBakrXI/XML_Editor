package com.editor.Json;

public class XML2JSON {
    private final StringBuilder jsonString = new StringBuilder();

    private final XMLTree tree;

    public XML2JSON(XMLTree xmlTree) {
        this.tree = xmlTree;
    }

    public String getJsonString() {
        jsonStringBuilder(tree.root);
        return "{\n"
                + jsonString
                + "}\n";
    }

    private boolean isNumeric(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void jsonStringBuilder(Node node) {
        if (tree.root == null || node == null) {
            return;
        }

        String key = "\"" + node.getName() + "\"";

        String value;
        if (isNumeric(node.getData())) {
            value = node.getData();
        } else {
            value = "\"" + node.getData() + "\"";
        }

        if (!node.getChildren().isEmpty()) {
            if (node.similarBrothers() != 0) {
                jsonObjectArrayNested(node, key);
            } else {
                jsonObjectNested(node, key);
            }
        } else {
            if (node.similarBrothers() != 0) {
                jsonObjectArray(node, key, value);
            } else {
                jsonObjectLeaf(node, key, value);
            }
            comma(node);
        }

        for (Node child : node.getChildren()) {
            jsonStringBuilder(child);
        }

        if (!node.getChildren().isEmpty()) {
            if (node.similarBrothers() != 0) {
                jsonObjectArrayClosing(node);
            } else {
                jsonObjectNestedClosing(node);
            }
            comma(node);
        } else {
            jsonObjectArrayLeafClosing(node);
        }
    }


    private String indent(int level) {
        return "\t".repeat(Math.max(0, level + 1));
    }

    private void comma(Node node) {
        if (!node.isLastChild()) jsonString.append(",");
        jsonString.append("\n");
    }

    // repeated lines
    private void jsonObjectArrayNested(Node node, String key) {
        if (node.isSimilarVisited()) {
            jsonString.append(indent(node.getDepth()))
                    .append(key)
                    .append(" : [\n")
                    .append(indent(node.getDepth()))
                    .append("{\n");
            node.setVisited(true);
        } else {
            jsonString.append(indent(node.getDepth()));
            jsonString.append(" {\n");
        }
    }

    private void jsonObjectNested(Node node, String key) {
        jsonString.append(indent(node.getDepth()));
        jsonString.append(key)
                .append(" : {\n");
    }

    private void jsonObjectArray(Node node, String key, String value) {
        if (node.isSimilarVisited()) {
            jsonString.append(indent(node.getDepth())).append(key).append(": [\n");
            jsonString.append(indent(node.getDepth())).append(value);
            node.setVisited(true);
        } else {
            jsonString.append(indent(node.getDepth()));
            jsonString.append(value);
        }
    }

    private void jsonObjectLeaf(Node node, String key, String value) {
        jsonString.append(indent(node.getDepth()));
        jsonString.append(key).append(" : ").append(value);
    }

    private void jsonObjectArrayClosing(Node node) {
        if (node.getIndex() == node.lastSimilarBrotherIndex()) {
            jsonString.append(indent(node.getDepth()))
                    .append("}\n")
                    .append(indent(node.getDepth()))
                    .append("]");
        } else {
            jsonString.append(indent(node.getDepth()))
                    .append("}");
        }
    }

    private void jsonObjectNestedClosing(Node node) {
        jsonString.append(indent(node.getDepth()))
                .append("}");
    }

    private void jsonObjectArrayLeafClosing(Node node) {
        if ((node.similarBrothers() != 0) && (node.getIndex() == node.lastSimilarBrotherIndex())) {
            jsonString.append(indent(node.getDepth()))
                    .append("]\n");
        }
    }
}
