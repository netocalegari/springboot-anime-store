DO
$$
BEGIN
   IF NOT EXISTS (SELECT FROM pg_catalog.pg_database WHERE datname = 'anime_store') THEN
      CREATE DATABASE anime_store;
   END IF;
END
$$;
