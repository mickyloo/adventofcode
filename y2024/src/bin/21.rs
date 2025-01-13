use adv_code_2024::*;
use anyhow::*;
use code_timing_macros::time_snippet;
use const_format::concatcp;
use itertools::Itertools;
use std::collections::HashMap;
use std::fs::File;
use std::io::{BufRead, BufReader};

const DAY: &str = "21"; // TODO: Fill the day
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
029A
980A
179A
456A
379A";

fn main() -> Result<()> {
    start_day(DAY);

    let mut lookup: HashMap<(char, char), String> = HashMap::new();
    lookup.insert(('A', '0'), "<A".to_string());
    lookup.insert(('A', '3'), "^A".to_string());
    lookup.insert(('A', '6'), "^^A".to_string());
    lookup.insert(('A', '9'), "^^^A".to_string());
    lookup.insert(('A', '2'), "^<A".to_string());
    lookup.insert(('A', '5'), "^^<A".to_string());
    lookup.insert(('A', '8'), "^^^<A".to_string());
    lookup.insert(('A', '1'), "^<<A".to_string());
    lookup.insert(('A', '4'), "^^<<A".to_string());
    lookup.insert(('A', '7'), "^^^<<A".to_string());
    lookup.insert(('0', '2'), "^A".to_string());
    lookup.insert(('2', '9'), "^^>A".to_string());
    lookup.insert(('9', 'A'), "vvvA".to_string());
    lookup.insert(('9', '8'), "<A".to_string());
    lookup.insert(('8', '0'), "vvvA".to_string());
    lookup.insert(('0', 'A'), ">A".to_string());
    lookup.insert(('1', '7'), "^^A".to_string());
    lookup.insert(('7', '9'), ">>A".to_string());
    lookup.insert(('4', '5'), ">A".to_string());
    lookup.insert(('5', '6'), ">A".to_string());
    lookup.insert(('6', 'A'), "vvA".to_string());
    lookup.insert(('3', '7'), "^^<<A".to_string());
    lookup.insert(('5', '4'), "<A".to_string());
    lookup.insert(('4', '0'), ">vvA".to_string());
    lookup.insert(('8', '3'), ">vvA".to_string());
    lookup.insert(('3', '9'), "^^A".to_string());
    lookup.insert(('6', '8'), "^<A".to_string());
    lookup.insert(('8', '2'), "vvA".to_string());
    lookup.insert(('2', 'A'), "v>A".to_string());
    lookup.insert(('2', '6'), ">^A".to_string());
    lookup.insert(('9', '7'), "<<A".to_string());
    lookup.insert(('7', '4'), "vA".to_string());
    lookup.insert(('4', 'A'), ">>vvA".to_string());

    lookup.insert(('A', '^'), "<A".to_string());
    lookup.insert(('A', '>'), "vA".to_string());
    lookup.insert(('A', 'v'), "v<A".to_string());
    lookup.insert(('A', '<'), "v<<A".to_string());
    lookup.insert(('^', 'A'), ">A".to_string());
    lookup.insert(('>', 'A'), "^A".to_string());
    lookup.insert(('v', 'A'), ">^A".to_string());
    lookup.insert(('<', 'A'), ">>^A".to_string());
    lookup.insert(('A', 'A'), "A".to_string());

    lookup.insert(('v', 'v'), "A".to_string());
    lookup.insert(('v', '>'), ">A".to_string());
    lookup.insert(('v', '<'), "<A".to_string());
    lookup.insert(('v', '^'), "^A".to_string());

    lookup.insert(('<', '<'), "A".to_string());
    lookup.insert(('<', '>'), ">>A".to_string());
    lookup.insert(('<', 'v'), ">A".to_string());
    lookup.insert(('<', '^'), ">^A".to_string());

    lookup.insert(('^', '^'), "A".to_string());
    lookup.insert(('^', '>'), "v>A".to_string());
    lookup.insert(('^', 'v'), "vA".to_string());
    lookup.insert(('^', '<'), "v<A".to_string());

    lookup.insert(('>', '>'), "A".to_string());
    lookup.insert(('>', '<'), "<<A".to_string());
    lookup.insert(('>', 'v'), "<A".to_string());
    lookup.insert(('>', '^'), "<^A".to_string());

    //region Part 1
    println!("=== Part 1 ===");

    fn run(input: &String, lookup: &HashMap<(char, char), String>) -> String {
        let mut result = input;
        for _ in 0..3 {
            let line = "A".to_owned() + result.as_str();
            result = &line
                .chars()
                .tuple_windows()
                .map(|(left, right)| &lookup[&(left, right)])
                .join("")
        }
        result.to_string()
    }

    fn part1<R: BufRead>(reader: R, lookup: HashMap<(char, char), String>) -> Result<usize> {
        let lines = reader.lines();
        let answer = lines
            .map(|line| {
                let line = line.unwrap();
                let seq = run(&line, &lookup);
                let num = line[0..3].to_string().parse::<usize>().unwrap();
                println!("{} {} {}", seq.len(), num, seq);
                num * seq.len()
            })
            .sum();

        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(0, part1(BufReader::new(TEST.as_bytes()), lookup.clone())?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file, lookup)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    // println!("\n=== Part 2 ===");
    //
    // fn part2<R: BufRead>(reader: R) -> Result<usize> {
    //     Ok(0)
    // }
    //
    // assert_eq!(0, part2(BufReader::new(TEST.as_bytes()))?);
    //
    // let input_file = BufReader::new(File::open(INPUT_FILE)?);
    // let result = time_snippet!(part2(input_file)?);
    // println!("Result = {}", result);
    //endregion

    Ok(())
}
