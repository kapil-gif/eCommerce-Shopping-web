package com.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.Model.Cart;
import com.Model.Product;

public class ProductDao {
    private Connection con;

    public ProductDao(Connection con) {
        this.con = con;
    }

    public List<Product> getAllProducts() {
        List<Product> products = new ArrayList<>();
        String query = "SELECT * FROM products";

        try (PreparedStatement pst = this.con.prepareStatement(query);
                ResultSet rs = pst.executeQuery()) {

            while (rs.next()) {
                Product row = new Product();
                row.setId(rs.getInt("id"));
                row.setName(rs.getString("name"));
                row.setCategory(rs.getString("category"));
                row.setPrice(rs.getDouble("price"));
                row.setImage(rs.getString("image"));
                products.add(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return products;
    }

    public Product getSingleProduct(int id) {
        Product row = null;
        String query = "SELECT * FROM products WHERE id=?";

        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            pst.setInt(1, id);

            try (ResultSet rs = pst.executeQuery()) {
                if (rs.next()) {
                    row = new Product();
                    row.setId(rs.getInt("id"));
                    row.setName(rs.getString("name"));
                    row.setCategory(rs.getString("category"));
                    row.setPrice(rs.getDouble("price"));
                    row.setImage(rs.getString("image"));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return row;
    }

    public double getTotalCartPrice(ArrayList<Cart> cartList) {
        double sum = 0.0;
        String query = "SELECT price FROM products WHERE id=?";

        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            for (Cart item : cartList) {
                pst.setInt(1, item.getId());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        sum += rs.getDouble("price") * item.getQuantity();
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sum;
    }

    public List<Cart> getCartProducts(ArrayList<Cart> cartList) {
        List<Cart> cartProducts = new ArrayList<>();
        String query = "SELECT * FROM products WHERE id=?";

        try (PreparedStatement pst = this.con.prepareStatement(query)) {
            for (Cart item : cartList) {
                pst.setInt(1, item.getId());
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        Cart row = new Cart();
                        row.setId(rs.getInt("id"));
                        row.setName(rs.getString("name"));
                        row.setCategory(rs.getString("category"));
                        row.setPrice(rs.getDouble("price") * item.getQuantity());
                        row.setQuantity(item.getQuantity());
                        cartProducts.add(row);
                    }
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cartProducts;
    }
}
