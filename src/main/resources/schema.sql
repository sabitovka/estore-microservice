CREATE TABLE IF NOT EXISTS counter (
	"name" varchar(75) NOT NULL,
	currentid int8 NULL,
	CONSTRAINT counter_pkey PRIMARY KEY (name)
);
