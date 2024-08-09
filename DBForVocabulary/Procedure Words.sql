create or replace procedure Words_Insert(p_Words Varchar(100), p_Translation_ Varchar(100))
language plpgsql
as $$
	begin
		insert into Words_Insert(Words, Translation_)
		values (p_Words, p_Translation_);
	end;
$$;	

create or replace procedure Words_Update(p_ID_Words Int, p_Words Varchar(100), p_Translation_ Varchar(100))
language plpgsql
as $$
	begin
		update Words set
		Words = p_Words,
		Translation_ = p_Translation_
		where ID_Words = p_ID_Words;
	end;
$$;

create or replace procedure Words_Delete(p_ID_Words Int)
language plpgsql
as $$
	begin
		delete from Words
		where ID_Words = p_ID_Words;
	end;
$$;