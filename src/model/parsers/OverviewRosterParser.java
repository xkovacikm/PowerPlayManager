package model.parsers;

import java.util.Arrays;

import model.Attributes;
import model.Player;
import model.Roster;
import model.RosterParser;
import model.Side;

public class OverviewRosterParser implements RosterParser
{
	@Override
	public Roster parseRoster(String textToParse) throws ParseException
	{
		try
		{
			String[] lines = textToParse.split("\n");

			Roster roster = new Roster();

			for (int i = 0; i < lines.length; i++)
			{
				String[] columns = lines[i].split("\t");

				Player player = new Player(
						parseName(columns[0]),
						parseSide(columns[16]),
						parseAttributes(Arrays.copyOfRange(columns, 6, 14)));

				roster.add(player);
			}

			return roster;
		}
		catch (Exception e)
		{
			throw new ParseException(e);
		}
	}

	private static String parseName(String text)
	{
		String[] split = text.split(" ");

		return String.format("%s %s", split[1], split[2]);
	}

	private static Side parseSide(String text)
	{
		text = text.trim();

		if (text.equals("U"))
		{
			return Side.UNIVERSAL;
		}
		else if (text.equals("L"))
		{
			return Side.LEFT;
		}
		else if (text.equals("R"))
		{
			return Side.RIGHT;
		}
		else
		{
			return Side.UNKNOWN;
		}
	}

	private static Attributes parseAttributes(String[] texts)
	{
		Attributes attributes = new Attributes();

		attributes.setGoa(Integer.parseInt(texts[0].trim()));
		attributes.setFip(Integer.parseInt(texts[1].trim()));
		attributes.setSho(Integer.parseInt(texts[2].trim()));
		attributes.setBlk(Integer.parseInt(texts[3].trim()));
		attributes.setPas(Integer.parseInt(texts[4].trim()));
		attributes.setTec(Integer.parseInt(texts[5].trim()));
		attributes.setSpe(Integer.parseInt(texts[6].trim()));
		attributes.setAgr(Integer.parseInt(texts[7].trim()));

		return attributes;
	}
}
