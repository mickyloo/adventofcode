use std::collections::HashMap;
use anyhow::*;
use std::fs::File;
use std::io::{BufRead, BufReader};
use code_timing_macros::time_snippet;
use const_format::concatcp;
use adv_code_2024::*;

const DAY: &str = "01";
const INPUT_FILE: &str = concatcp!("input/", DAY, ".txt");

const TEST: &str = "\
3   4
4   3
2   5
1   3
3   9
3   3
";

fn main() -> Result<()> {
    start_day(DAY);

    //region Part 1
    println!("\n=== Part 1 ===");
    fn part1<R: BufRead>(reader: R) -> Result<i32> {
        let mut l1: Vec<i32> = Vec::new();
        let mut l2: Vec<i32> = Vec::new();
        reader.lines().flatten()
            .for_each(|line| {
                let mut cols = line.split_whitespace();
                l1.push(cols.next().unwrap().parse::<i32>().unwrap());
                l2.push(cols.next().unwrap().parse::<i32>().unwrap());
            });

        l1.sort();
        l2.sort();
        let mut answer = 0;
        for (a, b) in l1.iter().zip(l2.iter()) {
            answer += (a - b).abs();
        }

        Ok(answer)
    }

    // TODO: Set the expected answer for the test input
    assert_eq!(11, part1(BufReader::new(TEST.as_bytes()))?);

    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part1(input_file)?);
    println!("Result = {}", result);
    //endregion

    //region Part 2
    println!("\n=== Part 2 ===");

    fn part2<R: BufRead>(reader: R) -> Result<i32> {
        let mut l1: Vec<i32> = Vec::new();
        let mut counter: HashMap<i32, i32> = HashMap::new();
        reader.lines().flatten()
            .for_each(|line| {
                let mut cols = line.split_whitespace();
                l1.push(cols.next().unwrap().parse::<i32>().unwrap());
                let entry = counter.entry(cols.next().unwrap().parse::<i32>().unwrap()).or_insert(0);
                *entry += 1;
            });

        let answer: i32 = l1.iter().map(|x| {
            x * counter.get(x).unwrap_or(&0)
        }).sum();
        Ok(answer)
    }
    //
    assert_eq!(31, part2(BufReader::new(TEST.as_bytes()))?);
    //
    let input_file = BufReader::new(File::open(INPUT_FILE)?);
    let result = time_snippet!(part2(input_file)?);
    println!("Result = {}", result);
    //endregion

    Ok(())
}
