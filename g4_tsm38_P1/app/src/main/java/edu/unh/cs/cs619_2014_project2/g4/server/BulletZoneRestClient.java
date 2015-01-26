package edu.unh.cs.cs619_2014_project2.g4.server;

import org.androidannotations.annotations.rest.Delete;
import org.androidannotations.annotations.rest.Get;
import org.androidannotations.annotations.rest.Post;
import org.androidannotations.annotations.rest.Put;
import org.androidannotations.annotations.rest.Rest;
import org.androidannotations.api.rest.RestClientErrorHandling;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestClientException;


/**
 * Created by Tay on 11/6/2014.
 */
@Rest(
        rootUrl	=	"http://stman1.cs.unh.edu:6191/games",
        converters	=	{	MappingJackson2HttpMessageConverter.class}
)

public interface BulletZoneRestClient extends RestClientErrorHandling {
    /**
     *	Join	the	server
     *	@return	tankId	in	a	wrapper	object
     */
    @Post("")
    LongWrapper join()	throws RestClientException;
    /**
     *	Get	the	current	status		(grid	&	timestamp)	of	the	game.
     *	@return	the	current	status	of	the	game	grid	and	timestamp	in	a	wrapper	object
     */
    @Get("")
    GridWrapper grid();
    /**
     *	Move	a	tank	one	step	in	a	given	direction.
     *	@param	tankId	is	the	id of	the	tank	to	move
     *	@param	direction	is	the	direction	of	movement
     *	@return	a	true/false	in	a	wrapper	object
     */
    @Put("/{tankId}/move/{direction}")
    BooleanWrapper move(long tankId,	byte direction);
    /**
     *	Turn	a	tank	to	a	given	direction.
     *	@param	tankId	is	the	id	of	the	tank	to	turn
     *	@param	direction	is	the	direction	of	turn
     *	@return	a	true/false	in	a	wrapper	object
     */
    @Put("/{tankId}/turn/{direction}")
    BooleanWrapper turn(long tankId,	byte direction);
    /**
     *	Fire	a	bullet.
     *	@param	tankId	is	the	id	of	the	tank	that	is	firing
     *	@return	a	true/false	in	a	wrapper	object
     */
    @Put("/{tankId}/fire")
    BooleanWrapper fire(long tankId);
    /**
     *	Leave	the	game.
     *	@param	tankId	is	the	id	of	the	tank	that	wants	to	leave
     *	@return	a	true/false	in	a	wrapper	object
     */
    @Delete("/{tankId}/leave")
    void leave(long tankId);
}




