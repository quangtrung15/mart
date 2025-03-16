package com.mart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mart.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	// Search products by category, price range, name, brand.
	@Query("SELECT p FROM products p WHERE " + "(p.category.id = :category_id OR :category_id IS NULL) AND "
			+ "((p.price >= :minPrice OR :minPrice IS NULL) AND (p.price <= :maxPrice OR :maxPrice IS NULL)) AND "
			+ "(p.name LIKE %:name% OR :name IS NULL) AND" + "(p.brand = :brand OR :brand IS NULL)")
	List<Product> findByCategoryIdAndPriceRangeAndNameAndBrand(@Param("category_id") Long category_id,
			@Param("minPrice") Double minPrice, @Param("maxPrice") Double maxPrice, @Param("name") String name,
			@Param("brand") String brand);

	// Display products by status.
	List<Product> findByStatus(String status);

	Optional<Product> findById(long productId);

}
